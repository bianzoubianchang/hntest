
import java.util.concurrent.TimeUnit;


import java.util.*;

import org.junit.Assert;

//import java.text.DecimalFormat;







import java.io.BufferedReader;
//������Ļ��ͼ��API
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.io.*;


public class transactionCheck{
	
	private WebDriver driver;
	private double account;
	private double accountAfterPurchase;
	private double accountAfterTransout;
	private double currentAccount;

	
	
	//getAccount()�������ڴ��ҵĹ��ʱ�ҳ���ȡ�˻����
	public double getAccount() throws Exception{
		String accountFresh_z=driver.findElement(By.id("account_int")).getText();	
		String accountFresh_l=driver.findElement(By.id("account_float")).getText();
		accountFresh_l=accountFresh_l.substring(0, 2);
		String accountFresh=accountFresh_z+accountFresh_l;
//		float f_accountFresh = Float.parseFloat(accountFresh);	
		double f_accountFresh=Double.parseDouble(accountFresh);
		
		return f_accountFresh;
	}
	
	//transDoubleToString()���ڽ�double����ת��ΪString�������ݡ���ʱδ�ҵ�ֱ�ӿ������õĿ⺯��
	public String transDoubleToString(double orgin) throws Exception{
		String trans=String.format("%.2f", orgin);
		return trans;
	}
	
	
	
	//getTotalRevenue()�������ڴ��ҵĹ��ʱ�ҳ���ȡ��ʷ����
	public float getTotalRevenue() throws Exception{
		String revenueFresh_z=driver.findElement(By.id("revenue_int")).getText();	
		String revenueFresh_l=driver.findElement(By.id("revenue_float")).getText();
		revenueFresh_l=revenueFresh_l.substring(0, 2);
		String revenueFresh=revenueFresh_z+revenueFresh_l;
		float f_revenueFresh = Float.parseFloat(revenueFresh);	
		return f_revenueFresh;
	}

	//isElementPresent()�������ڼ��һ��Ԫ���Ƿ����
	//��ʱ�ɲ��ø÷���
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
	
	@DataProvider(name="transout")
	public Object[][] outMoney(){
		return new Object[][]{
				{"100"},
				{"200"},
		};
	}
		
	@DataProvider(name="transin")
	public Object[][] inMoney(){
		return new Object[][]{
				{"100"},
				{"300"},
				{"20"},

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


/*ʹ��@Parameters��ȡ����*/
 
	@Test
	@Parameters({"username","password","name"})

	//Ŀ�ģ���֤��½����
	//ʹ�õ�½���������ʾ��֤��½״̬
	public void userlogin(String username_para,String password,String name) throws Exception{

		//ͨ���ı����placeholder����У���û���/�����ı����Ĭ����ʾ���ֵ���ȷ��
		String usernameTooltip=driver.findElement(By.id("txt_username")).getAttribute("placeholder");
		String passwordTooltip=driver.findElement(By.id("txt_pwd")).getAttribute("placeholder");
		Assert.assertEquals("�û���Ϊ�ֻ���", usernameTooltip);
		Assert.assertEquals("6-20λ����", passwordTooltip);
		
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
	
	
	//Ŀ�ģ���֤��ҳ�˻�����ʷ���桢����ؽ��������桢������桢�����껯�����ʵ�����
	//��ʱ�ȶ�ȡ���ݣ���0�Ƚϡ������˻���֤�������ݡ�
	
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
		System.out.println("�˻��ܶ"+totalAccount);
		System.out.println("����ؽ�"+f_available);
		System.out.println("��ʷ���棺"+totalRevenue);
		System.out.println("�������棺"+f_revenue);
		System.out.println("������棺"+f_rate);
		System.out.println("�����껯�����ʣ�"+f_incomerate);
		System.out.println("-----------------------");
		
		Assert.assertNotEquals(0.00, totalAccount, 0);
		Assert.assertNotEquals(0.00, f_available, 0);
		Assert.assertNotEquals(0.00, f_rate, 0);
		Assert.assertNotEquals(0.00, f_incomerate, 0);
	}

	



	
	@Test(invocationCount=1,dataProvider="transin")
	//Ŀ�ģ���֤������
	//���Ȼ�ȡ��ǰ�˻���Ȼ��ִͬ�й��������ݽ��бȽ�
	public void purchase(String transin) throws Exception{

		boolean purchaseResult=true;
		
		account=getAccount();
		driver.findElement(By.linkText("����")).click();
		
		//��ȡ����������ںͿ��������
		String incomeDateTips=driver.findElement(By.id("income_date")).getText();
		String transoutDateTips=driver.findElement(By.id("can_trans_out_date")).getText();
		
		Double double_transin=Double.valueOf(transin);
		
		if(double_transin>=100){
			
			if(double_transin+account<=4500000){
		

		driver.findElement(By.id("money_in")).clear();
//		driver.findElement(By.id("money_in")).sendKeys("100");
		driver.findElement(By.id("money_in")).sendKeys(transin);
		
		
		driver.findElement(By.id("income_date")).click();
		String monkeyinTestClass=driver.findElement(By.id("money_in")).getAttribute("class");
		Assert.assertEquals("input_public input_correct", monkeyinTestClass);
		driver.findElement(By.id("btn_submit")).click();
		
		String purchaseTips=driver.findElement(By.xpath("//html/body/div[6]/div/div/div[2]")).getText();
		
		
		if(driver.findElement(By.xpath("//html/body/div[6]/ul/li[1]")).getText().equals("����ʧ��")){
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
	System.out.println("����ʧ��ʱ��ԭ�˻��ܽ�"+account);	
		}
		
		
		
		String str_accountSum=transDoubleToString(double_accountSum);
		
		accountAfterPurchase=getAccount();
		String str_accountAfterPurchase=transDoubleToString(accountAfterPurchase);

		
		
		Assert.assertEquals(str_accountSum, str_accountAfterPurchase);
		
		System.out.println("��������ʾ��Ϣ��"+purchaseTips);

		System.out.println("����ǰ���˻��ܶ"+str_account);
		System.out.println("����"+transin+"����˻��ܶ"+str_accountAfterPurchase);
		
		System.out.println("����ǰ���������������ʾ��"+incomeDateTips);
		System.out.println("����ǰ�Ŀ����������ʾ��"+transoutDateTips);
		System.out.println("-----------------------");
		
		transaction();
		
			}
			else{
				System.out.println("������"+double_transin+"�ӵ�ǰ�ܽ��"+account+"����450��");
				System.out.println("-----------------------");
				driver.findElement(By.xpath("//html/body/div[1]/div[2]/div/div[2]/ul/li[1]/a/div")).click();
			}
		
		
		}

		else{
			System.out.println("������"+double_transin+"������Լ��");
			System.out.println("-----------------------");
			driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/ul/li[2]/a/div")).click();
		
		}

	}

	


//	@Test(dependsOnMethods={"purchase"})
//	@Test
	public void transaction() throws Exception {
		driver.findElement(By.linkText("������ϸ")).click();
		driver.findElement(By.xpath("//html/body/div[2]/div[5]/div/div[2]/div/div/div[2]/div/table/tbody/tr[2]/td[4]/a/img")).click();
		String getTooltips=driver.findElement(By.xpath("/html/body/div[2]/div[5]/div/div[2]/div/div/div[2]/div/table/tbody/tr[2]/td[4]/span")).getText();
		
		driver.findElement(By.xpath("//html/body/div[2]/div[3]/div[2]/div[2]/span/div[3]")).click();
		String getOnlineCount=driver.findElement(By.xpath("//html/body/div[2]/div[3]/div[2]/div[2]/span/label/span")).getText();
		
		System.out.println("��;��"+getOnlineCount);
		System.out.println("���׳ɹ�ʱ����ʾ��"+getTooltips);
		System.out.println("-----------------------");
		
	}
	
//�������ι���ͽ��׼�¼

		
	@Test(invocationCount=1,dataProvider="transout")
	//Ŀ�ģ���֤��ع���
	//������Դ��ȡ��ؽ������ؽ��>����ؽ��򽫿���ؽ����Ϊ������ؽ�
	//���Ȼ�ȡ��ǰ�˻���Ȼ��ִͬ����غ�����ݽ��бȽ�

	public void trans_out_dev(String transoutMoney) throws Exception{

		boolean transoutResult=true;
		
		driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/ul/li[2]/a/div")).click();
		currentAccount=getAccount();
	
		
		System.out.println("���ǰ���˻��ܶ"+currentAccount);
	
		Double f_transoutMoney=Double.parseDouble(transoutMoney);
		
		
		driver.findElement(By.linkText("���")).click();
		
		String canTransoutMoney=driver.findElement(By.id("can_out_money")).getText();
		double f_canTransoutMoney=Double.parseDouble(canTransoutMoney);
		System.out.println("����ؽ�"+transDoubleToString(f_canTransoutMoney));

		if(f_transoutMoney>f_canTransoutMoney){
//			System.out.println("��������ڿ���ؽ��ʱ��ת������");
			transoutMoney=canTransoutMoney;
			f_transoutMoney=f_canTransoutMoney;
		}

		
		//��ȡ��ؽ���ı����class����ֵ
		String getMoneyTextClass=driver.findElement(By.id("money_out")).getAttribute("class");
		

		if(f_canTransoutMoney!=0){		
			

			
		if((!getMoneyTextClass.equals("input_public")) || (f_canTransoutMoney>100)){

			System.out.println("������ؽ�"+transoutMoney);
			driver.findElement(By.id("money_out")).sendKeys(transoutMoney);

		}
		
		
		
	
		driver.findElement(By.id("btn_submit")).click();	
			
		
		//ͨ���鿴��ѡ��Ԫ�ص��������ж������ִ�����̣�
		List<WebElement> radio=driver.findElements(By.ByName.name("radio_transout"));
		int radionum=radio.size();	


	if(radionum>1){
			
//			driver.findElement(By.id("radio_realtime")).click();
			int select=(int)(Math.random()*2);
			radio.get(select).click();
			File srcfileSelect=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcfileSelect, new File("e:\\ScreenForTesting\\1\\test2.png"));
			driver.findElement(By.xpath("//html/body/div[6]/div/div[3]/a")).click();	
			
System.out.println(driver.findElement(By.xpath("//html/body/div[8]/ul/li[1]")).getText());			
			
			if(driver.findElement(By.xpath("//html/body/div[8]/ul/li[1]")).getText().equals("���ʧ��")){
				driver.findElement(By.xpath("//html/body/div[8]/div/div[4]/a")).click();
				transoutResult=false;
			}
			else{
				driver.findElement(By.xpath("//html/body/div[8]/div/div[3]/a")).click();
			}
	
		}
		else{
			
//			driver.findElement(By.id("radio_normal")).click();
			radio.get(0).click();
			driver.findElement(By.xpath("//html/body/div[6]/div/div[2]/a")).click();	
	

			driver.findElement(By.xpath("//html/body/div[8]/div/div[3]/a")).click();
		}	
		
		accountAfterTransout=getAccount();
		String tarns_accountAfterTransout=transDoubleToString(accountAfterTransout);
		

		String checkAccount=transDoubleToString(currentAccount-(f_transoutMoney*100)/100);

	if(!transoutResult){

		checkAccount=transDoubleToString(currentAccount);
System.out.println("���ʧ��ʱ��ԭ�˻��ܽ�"+checkAccount);	
	}
		
		Assert.assertEquals(checkAccount, tarns_accountAfterTransout);

		System.out.println("��غ���˻��ܶ�"+tarns_accountAfterTransout);
		System.out.println("-----------------------");
		transaction();
		}	
		else{
			System.out.println("���˻��޿���صĽ��");		
		}		
		

	}	
	

	
	
	
	@AfterTest
	public void tearDown() throws Exception{
		 driver.quit();
	}
	
	
	
}