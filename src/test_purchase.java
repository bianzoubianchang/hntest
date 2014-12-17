
import java.util.concurrent.TimeUnit;


import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;




public class test_purchase{
	
	private WebDriver driver;
	private double account;
	private double accountAfterPurchase;


	//transDoubleToString()用于将double数据转换为String类型数据。暂时未找到直接可以引用的库函数
	public String transDoubleToString(double orgin) throws Exception{
		String trans=String.format("%.2f", orgin);
		return trans;
	}


	//isElementPresent()函数用于检测一个元素是否存在
	//暂时吧采用该方法
	public boolean isElementPresent(By by){
		try{
			driver.findElement(by);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	//getAccount()函数用于从我的工资宝页面获取账户金额
	public double getAccount() throws Exception{
		String accountFresh_z=driver.findElement(By.id("account_int")).getText();	
		String accountFresh_l=driver.findElement(By.id("account_float")).getText();
		accountFresh_l=accountFresh_l.substring(0, 2);
		String accountFresh=accountFresh_z+accountFresh_l;
//		float f_accountFresh = Float.parseFloat(accountFresh);	
		double f_accountFresh=Double.parseDouble(accountFresh);
		
		return f_accountFresh;
	}
	
	
	
	//getTotalRevenue()函数用于从我的工资宝页面获取历史收益
	public float getTotalRevenue() throws Exception{
		String revenueFresh_z=driver.findElement(By.id("revenue_int")).getText();	
		String revenueFresh_l=driver.findElement(By.id("revenue_float")).getText();
		revenueFresh_l=revenueFresh_l.substring(0, 2);
		String revenueFresh=revenueFresh_z+revenueFresh_l;
		float f_revenueFresh = Float.parseFloat(revenueFresh);	
		return f_revenueFresh;
	}


		
	@DataProvider(name="transin")
	public Object[][] inMoney(){
		return new Object[][]{
				{"2000000"},

		};
	}	
		
		

	@BeforeTest
	@Parameters({"host_test"})
	public void setup(@Optional("http://10.211.4.107/") String host_test) throws Exception{
	driver = new FirefoxDriver();
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(50,TimeUnit.SECONDS);
	driver.get(host_test);
	}


/*使用@Parameters获取参数*/
 
	@Test
	@Parameters({"username","password","name"})

	//目的：验证登陆功能
	//使用登陆后的姓名显示验证登陆状态
	public void userlogin(String username_para,String password,String name) throws Exception{

		//通过文本框的placeholder属性校验用户名/密码文本框的默认提示文字的正确性
		String usernameTooltip=driver.findElement(By.id("txt_username")).getAttribute("placeholder");
		String passwordTooltip=driver.findElement(By.id("txt_pwd")).getAttribute("placeholder");
		Assert.assertEquals("用户名为手机号", usernameTooltip);
		Assert.assertEquals("6-20位密码", passwordTooltip);
		
		driver.findElement(By.id("txt_username")).click();
		driver.findElement(By.id("txt_username")).clear();
		driver.findElement(By.id("txt_username")).sendKeys(username_para);
		driver.findElement(By.id("txt_pwd")).click();
		driver.findElement(By.id("txt_pwd")).clear();
		driver.findElement(By.id("txt_pwd")).sendKeys(password);
		driver.findElement(By.id("greenSubmit")).click();
	

		String username=driver.findElement(By.xpath("//html/body/div[1]/div[1]/div/div[2]/ul/li[1]/span")).getText();	
		
		Assert.assertEquals(name, username);

	}
	
	
	//目的：验证首页账户金额、历史收益、可赎回金额、昨日收益、万份收益、七日年化收益率的数据
	//暂时先读取数据，和0比较。测试账户保证都有数据。
	
	@Test
	public void getvalue() throws Exception{
		double totalAccount=getAccount();
		float totalRevenue=getTotalRevenue();
		
		String available=driver.findElement(By.xpath("//html/body/div[4]/div[3]/div[2]/div[1]/div[2]/div[1]")).getText();
		
		float f_available = Float.parseFloat(available);
		
		String revenue=driver.findElement(By.xpath("//html/body/div[4]/div[3]/div[2]/div[2]/div[2]/div[1]")).getText();
		
		float f_revenue=Float.parseFloat(revenue);
		
		String rate=driver.findElement(By.xpath("//html/body/div[4]/div[3]/div[2]/div[3]/div[2]")).getText();
		float f_rate=Float.parseFloat(rate);

		String incomerate=driver.findElement(By.xpath("//html/body/div[4]/div[3]/div[2]/div[4]/div[2]")).getText();
		float f_incomerate=Float.parseFloat(incomerate);
		

		System.out.println("-----------------------");
		System.out.println("账户总额："+totalAccount);
		System.out.println("可赎回金额："+f_available);
		System.out.println("历史收益："+totalRevenue);
		System.out.println("昨日收益："+f_revenue);
		System.out.println("万份收益："+f_rate);
		System.out.println("七日年化收益率："+f_incomerate);
		System.out.println("-----------------------");
		
		Assert.assertNotEquals(0.00, totalAccount, 0);
		Assert.assertNotEquals(0.00, f_available, 0);
		Assert.assertNotEquals(0.00, f_rate, 0);
		Assert.assertNotEquals(0.00, f_incomerate, 0);
	}

	



	
	@Test(invocationCount=1,dataProvider="transin")
	//目的：验证购买功能
	//首先获取当前账户金额，然后同执行购买后的数据进行比较
	public void purchase(String transin) throws Exception{

		boolean purchaseResult=true;
		
		account=getAccount();
		driver.findElement(By.linkText("购买")).click();
		
		//获取收益计算日期和可赎回日期
		String incomeDateTips=driver.findElement(By.id("income_date")).getText();
		String transoutDateTips=driver.findElement(By.id("can_trans_out_date")).getText();
		
		Double double_transin=Double.valueOf(transin);
		
		if(double_transin>=100){
			
			if(double_transin+account<=100000000){
		

		driver.findElement(By.id("money_in")).clear();
//		driver.findElement(By.id("money_in")).sendKeys("100");
		driver.findElement(By.id("money_in")).sendKeys(transin);
		
		
		driver.findElement(By.id("income_date")).click();
		String monkeyinTestClass=driver.findElement(By.id("money_in")).getAttribute("class");
		Assert.assertEquals("input_public input_correct", monkeyinTestClass);
		driver.findElement(By.id("btn_submit")).click();
		
		String purchaseTips=driver.findElement(By.xpath("//html/body/div[6]/div/div/div[2]")).getText();
		
		
		if(driver.findElement(By.xpath("//html/body/div[6]/ul/li[1]")).getText().equals("购买失败")){
			driver.findElement(By.xpath("//html/body/div[6]/div/div[4]/a")).click();
			purchaseResult=false;
		}
		else{
			driver.findElement(By.xpath("//html/body/div[6]/div/div[3]/a")).click();
		}


		String str_account=transDoubleToString(account);
		

		Double double_accountSum=account+double_transin;
	
		
		if(!purchaseResult){

			double_accountSum=account;
	System.out.println("购买失败时，原账户总金额："+account);	
		}
		
		
		
		String str_accountSum=transDoubleToString(double_accountSum);
		
		accountAfterPurchase=getAccount();
		String str_accountAfterPurchase=transDoubleToString(accountAfterPurchase);

		
		
		Assert.assertEquals(str_accountSum, str_accountAfterPurchase);
		
		System.out.println("购买后的提示信息："+purchaseTips);

		System.out.println("购买前的账户总额："+str_account);
		System.out.println("购买"+transin+"后的账户总额："+str_accountAfterPurchase);
		
		System.out.println("购买前的收益计算日期提示："+incomeDateTips);
		System.out.println("购买前的可赎回日期提示："+transoutDateTips);
		System.out.println("-----------------------");
		
		transaction();
		
			}
			else{
				System.out.println("购买金额"+double_transin+"加当前总金额"+account+"大于1亿");
				System.out.println("-----------------------");
				driver.findElement(By.xpath("//html/body/div[1]/div[2]/div/div[2]/ul/li[1]/a/div")).click();
			}
		
		
		}

		else{
			System.out.println("购买金额"+double_transin+"不符合约束");
			System.out.println("-----------------------");
			driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/ul/li[2]/a/div")).click();
		
		}

	}

	

//在购买交易后，查看交易记录详情。需要考虑规避交易失败的情景。
	@Test(dependsOnMethods={"purchase"})
	//
	public void transaction() throws Exception {
		driver.findElement(By.linkText("交易明细")).click();
		//暂时通过获取第一行的方式取得本次交易的详情。可能不够灵活。
		driver.findElement(By.xpath("//html/body/div[2]/div[5]/div/div[2]/div/div/div[2]/div[1]/table/tbody/tr[2]/td[5]/a")).click();
		String getTransNo=driver.findElement(By.xpath("//html/body/div[6]/div/div/div[1]/div/div/div")).getText();
		String getTransType=driver.findElement(By.xpath("//html/body/div[6]/div/div/div[3]/div/div/div")).getText();
		String getTransData=driver.findElement(By.xpath("//html/body/div[6]/div/div/div[4]/div/div/div")).getText();
		String getTransStatus=driver.findElement(By.xpath("//html/body/div[6]/div/div/div[5]/div/div/div")).getText();
		String getTooltips=driver.findElement(By.xpath("//html/body/div[6]/div/div/div[6]/div/div/div")).getText();

		System.out.println("交易号："+getTransNo);
		System.out.println("交易类型："+getTransType);
		System.out.println("交易金额："+getTransData);
		System.out.println("交易状态："+getTransStatus);
		System.out.println("交易到账时间提示："+getTooltips);
		
		
		driver.findElement(By.xpath("//html/body/div[2]/div[3]/div[2]/div[2]/span/div[3]")).click();
		String getOnlineCount=driver.findElement(By.xpath("//html/body/div[2]/div[3]/div[2]/div[2]/span/label/span")).getText();
		
		System.out.println("在途金额："+getOnlineCount);

		
	}
	

	
	
	@AfterTest
	public void tearDown() throws Exception{
		 driver.quit();
	}
	
	
	
}