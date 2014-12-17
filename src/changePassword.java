
/*
 * 脚本说明：
 * 该脚本主要用于验证更改密码的基本功能
 * 
 * 功能点：
 * 1.登录后验证用户身份
 * 2.更改密码的基本功能。
 * 
 * 执行前提条件：
 * 账号可用
 * 
 * 基本执行：
 * 登录验证身份，更改密码，分别使用新旧密码登录。操作完成后，还原数据
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

public class changePassword {
	private WebDriver driver;
	
	//指定测试账号数据源：用户名、密码、新密码
	@DataProvider(name="loginInfo")
	public Object[][] loginInfo(){
		return new Object[][]{
				{"18800000001","111111","222222","王一"},
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

	
	@Test(invocationCount=1,dataProvider="loginInfo")
	public void changepwd(String username_para,String password,String new_password,String name) throws Exception {
		
		System.out.println("****验证更改密码的基本功能****");
		System.out.println("测试数据说明如下：");
		System.out.println("用户名："+username_para+"，密码："+password+"，新密码："+new_password);
		
		driver.findElement(By.id("txt_username")).click();
		driver.findElement(By.id("txt_username")).clear();
		driver.findElement(By.id("txt_username")).sendKeys(username_para);
		driver.findElement(By.id("txt_pwd")).click();
		driver.findElement(By.id("txt_pwd")).clear();
		driver.findElement(By.id("txt_pwd")).sendKeys(password);
		driver.findElement(By.id("greenSubmit")).click();
		
		//使用姓名验证用户身份
		String username=driver.findElement(By.xpath("//html/body/div[1]/div[1]/div/div[2]/ul/li[1]/span")).getText();
		Assert.assertEquals(name, username);
		
		driver.findElement(By.linkText("修改密码")).click();
		driver.findElement(By.id("oldPassword")).sendKeys(password);
		driver.findElement(By.id("newPassword")).sendKeys(new_password);
		driver.findElement(By.id("newCxfPassword")).sendKeys(new_password);
		driver.findElement(By.id("blueSubmit")).click();
		
		//获取更改密码后的提示
		String tooltips=driver.findElement(By.xpath("//html/body/div[4]/div/div[2]/form/div/p[1]")).getText();
		System.out.println("更改密码后的提示");
		System.out.println("-----------------------");
		System.out.println(tooltips);
		
		//使用原密码、新密码分别登录
		driver.findElement(By.linkText("退出")).click();
		driver.findElement(By.id("txt_username")).click();
		driver.findElement(By.id("txt_username")).clear();
		driver.findElement(By.id("txt_username")).sendKeys(username_para);
		driver.findElement(By.id("txt_pwd")).click();
		driver.findElement(By.id("txt_pwd")).clear();
		driver.findElement(By.id("txt_pwd")).sendKeys(password);
		driver.findElement(By.id("greenSubmit")).click();
		Thread.sleep(2000);	
		String login_tooltips=driver.findElement(By.xpath("//html/body/div[3]/div[2]/div[1]/div[2]/div")).getText();
		System.out.println("-----------------------");
		System.out.println("使用旧密码登录失败的提示");	
		System.out.println(login_tooltips);	
		System.out.println("-----------------------");
		
		driver.findElement(By.id("txt_username")).click();
		driver.findElement(By.id("txt_username")).clear();
		driver.findElement(By.id("txt_username")).sendKeys(username_para);
		driver.findElement(By.id("txt_pwd")).click();
		driver.findElement(By.id("txt_pwd")).clear();
		driver.findElement(By.id("txt_pwd")).sendKeys(new_password);
		driver.findElement(By.id("greenSubmit")).click();		
		
		//登录后还原密码
		driver.findElement(By.linkText("修改密码")).click();
		driver.findElement(By.id("oldPassword")).sendKeys(new_password);
		driver.findElement(By.id("newPassword")).sendKeys(password);
		driver.findElement(By.id("newCxfPassword")).sendKeys(password);
		driver.findElement(By.id("blueSubmit")).click();	
		
	}

	@AfterTest
	public void tearDown() throws Exception{
		 driver.quit();

	}
	
}
