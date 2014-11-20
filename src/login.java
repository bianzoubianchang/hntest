
/*
 * �ű�˵����
 * �ýű���Ҫ������֤��¼�����������
 * 
 * ���ܵ㣺
 * 1.��������¼5�Σ�����2�δ����¼����ʾʣ��Ĵ����¼����
 * 2.�����¼��5�κ󣬶����˻�
 * 3.��ȷ��¼�󣬻ָ������¼�Ĵ���
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
	

	//ָ�������˺�����Դ���û����������������ȷ����
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
		
		System.out.println("****��֤�����¼�Ĵ�������****");
		System.out.println("��������˵�����£�");
		System.out.println("�û�����"+username_para+"���������룺"+password+"����ȷ���룺"+true_password);

		String usernameTooltip=driver.findElement(By.id("txt_username")).getAttribute("placeholder");
		String passwordTooltip=driver.findElement(By.id("txt_pwd")).getAttribute("placeholder");
		//��֤�û����������ı����Ĭ����ʾ	
		Assert.assertEquals("�û���Ϊ�ֻ���", usernameTooltip);
		Assert.assertEquals("6-20λ����", passwordTooltip);
		
		int n=0;		
		for(int i=1;i<=5;i++)	{
			driver.findElement(By.id("txt_username")).click();
			driver.findElement(By.id("txt_username")).clear();
			driver.findElement(By.id("txt_username")).sendKeys(username_para);
			driver.findElement(By.id("txt_pwd")).click();
			driver.findElement(By.id("txt_pwd")).clear();
			
			if(i==5 && n==0){
			//��5�ε�¼�ɹ��󣬻ָ����Ե�¼����	
			driver.findElement(By.id("txt_pwd")).sendKeys(true_password);
			Thread.sleep(5000);	
			driver.findElement(By.id("greenSubmit")).click();
			driver.findElement(By.linkText("�˳�")).click();	
			
			System.out.println("-----------------------");
			System.out.println("ͬһ�˻��ɹ���¼��Ӧ��ԭ5�δ����¼���᣺");
			System.out.println("-----------------------");
			System.out.println("-----------------------");
			
			//��¼�ɹ������½��д����¼����֤�����¼5�κ����ʾ
			n++;
			i=0;
			continue;
			}
			else{
				driver.findElement(By.id("txt_pwd")).sendKeys(password);
				driver.findElement(By.id("greenSubmit")).click();
			}
			
	
			//��ȡ����ӡÿ�δ����¼����ʾ��Ϣ	
			Thread.sleep(2000);	
			String tooltips=driver.findElement(By.xpath("//html/body/div[3]/div[2]/div[1]/div[2]/div")).getText();
			
			System.out.println("��"+i+"�δ����¼����ʾ");
			System.out.println(tooltips);
			System.out.println("-----------------------");
		}

	}
	

	@AfterTest
	public void tearDown() throws Exception{
		 driver.quit();

	}
	
	
	
}