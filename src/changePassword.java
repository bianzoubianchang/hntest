
/*
 * �ű�˵����
 * �ýű���Ҫ������֤��������Ļ�������
 * 
 * ���ܵ㣺
 * 1.��¼����֤�û����
 * 2.��������Ļ������ܡ�
 * 
 * ִ��ǰ��������
 * �˺ſ���
 * 
 * ����ִ�У�
 * ��¼��֤��ݣ��������룬�ֱ�ʹ���¾������¼��������ɺ󣬻�ԭ����
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
	
	//ָ�������˺�����Դ���û��������롢������
	@DataProvider(name="loginInfo")
	public Object[][] loginInfo(){
		return new Object[][]{
				{"18800000001","111111","222222","��һ"},
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
		
		System.out.println("****��֤��������Ļ�������****");
		System.out.println("��������˵�����£�");
		System.out.println("�û�����"+username_para+"�����룺"+password+"�������룺"+new_password);
		
		driver.findElement(By.id("txt_username")).click();
		driver.findElement(By.id("txt_username")).clear();
		driver.findElement(By.id("txt_username")).sendKeys(username_para);
		driver.findElement(By.id("txt_pwd")).click();
		driver.findElement(By.id("txt_pwd")).clear();
		driver.findElement(By.id("txt_pwd")).sendKeys(password);
		driver.findElement(By.id("greenSubmit")).click();
		
		//ʹ��������֤�û����
		String username=driver.findElement(By.xpath("//html/body/div[1]/div[1]/div/div[2]/ul/li[1]/span")).getText();
		Assert.assertEquals(name, username);
		
		driver.findElement(By.linkText("�޸�����")).click();
		driver.findElement(By.id("oldPassword")).sendKeys(password);
		driver.findElement(By.id("newPassword")).sendKeys(new_password);
		driver.findElement(By.id("newCxfPassword")).sendKeys(new_password);
		driver.findElement(By.id("blueSubmit")).click();
		
		//��ȡ������������ʾ
		String tooltips=driver.findElement(By.xpath("//html/body/div[4]/div/div[2]/form/div/p[1]")).getText();
		System.out.println("������������ʾ");
		System.out.println("-----------------------");
		System.out.println(tooltips);
		
		//ʹ��ԭ���롢������ֱ��¼
		driver.findElement(By.linkText("�˳�")).click();
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
		System.out.println("ʹ�þ������¼ʧ�ܵ���ʾ");	
		System.out.println(login_tooltips);	
		System.out.println("-----------------------");
		
		driver.findElement(By.id("txt_username")).click();
		driver.findElement(By.id("txt_username")).clear();
		driver.findElement(By.id("txt_username")).sendKeys(username_para);
		driver.findElement(By.id("txt_pwd")).click();
		driver.findElement(By.id("txt_pwd")).clear();
		driver.findElement(By.id("txt_pwd")).sendKeys(new_password);
		driver.findElement(By.id("greenSubmit")).click();		
		
		//��¼��ԭ����
		driver.findElement(By.linkText("�޸�����")).click();
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
