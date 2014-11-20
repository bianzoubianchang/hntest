
/*
 * 脚本说明：
 * 该脚本主要用于验证登录错误次数限制
 * 
 * 功能点：
 * 1.允许错误登录5次，超过2次错误登录后提示剩余的错误登录次数
 * 2.错误登录达5次后，冻结账户
 * 3.正确登录后，恢复错误登录的次数
 */

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
	

	//指定测试账号数据源：用户名、错误密码和正确密码
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
		
		System.out.println("****验证错误登录的次数限制****");
		System.out.println("测试数据说明如下：");
		System.out.println("用户名："+username_para+"，错误密码："+password+"，正确密码："+true_password);

		String usernameTooltip=driver.findElement(By.id("txt_username")).getAttribute("placeholder");
		String passwordTooltip=driver.findElement(By.id("txt_pwd")).getAttribute("placeholder");
		//验证用户名和密码文本框的默认提示	
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
			//第5次登录成功后，恢复尝试登录次数	
			driver.findElement(By.id("txt_pwd")).sendKeys(true_password);
			Thread.sleep(5000);	
			driver.findElement(By.id("greenSubmit")).click();
			driver.findElement(By.linkText("退出")).click();	
			
			System.out.println("-----------------------");
			System.out.println("同一账户成功登录后，应还原5次错误登录机会：");
			System.out.println("-----------------------");
			System.out.println("-----------------------");
			
			//登录成功后，重新进行错误登录，验证错误登录5次后的提示
			n++;
			i=0;
			continue;
			}
			else{
				driver.findElement(By.id("txt_pwd")).sendKeys(password);
				driver.findElement(By.id("greenSubmit")).click();
			}
			
	
			//获取并打印每次错误登录的提示信息	
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