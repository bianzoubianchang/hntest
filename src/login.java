
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class login{
	
	private WebDriver driver;
	

	@DataProvider(name="loginErrorAttempt")
	public Object[][] loginError(){
		return new Object[][]{
				{"19900000002","111112","111111"},
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


	
	@Test(invocationCount=1,dataProvider="loginErrorAttempt")

	public void userlogin(String username_para,String password,String true_password) throws Exception{

		String usernameTooltip=driver.findElement(By.id("txt_username")).getAttribute("placeholder");
		String passwordTooltip=driver.findElement(By.id("txt_pwd")).getAttribute("placeholder");
		Assert.assertEquals("用户名为手机号", usernameTooltip);
		Assert.assertEquals("6-20位密码", passwordTooltip);
		
		int n=0;		
		for(int i=1;i<=5;i++)	{
			driver.findElement(By.id("txt_username")).click();
			driver.findElement(By.id("txt_username")).clear();
			driver.findElement(By.id("txt_username")).sendKeys(username_para);
			driver.findElement(By.id("txt_pwd")).click();
			driver.findElement(By.id("txt_pwd")).clear();
			
			if(i==5 && n==0){
			driver.findElement(By.id("txt_pwd")).sendKeys(true_password);
			Thread.sleep(5000);	
			driver.findElement(By.id("greenSubmit")).click();
			driver.findElement(By.linkText("退出")).click();	
			
			System.out.println("-----------------------");
			System.out.println("同一账户成功登录后，应还原5次错误登录机会：");
			System.out.println("-----------------------");
			System.out.println("-----------------------");
			
			n++;
			i=0;
			continue;
			}
			else{
				driver.findElement(By.id("txt_pwd")).sendKeys(password);
				driver.findElement(By.id("greenSubmit")).click();
			}
			
	
			Thread.sleep(2000);			
			String tooltips=driver.findElement(By.xpath("//html/body/div[3]/div[2]/div[1]/div[2]/div")).getText();
			
			System.out.println("第"+i+"次错误登录的提示");
			System.out.println(tooltips);
			System.out.println("-----------------------");
		}

	}
	

	@AfterTest
	public void tearDown() throws Exception{
		 driver.quit();

	}
	
	
	
}