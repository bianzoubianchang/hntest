import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;



public class LoginPage {
	
	public static final String TITLE="华能成长宝";
	public static final String USERNAME_TEXT="txt_username";
	public static final String PASSWORD_TEXT="txt_pwd";
	public static final String LOGIN_BUTTON="greenSubmit";	
	
	private WebDriver driver;
	private String url;
	
	public LoginPage(WebDriver driver,String url){
		this.driver=driver;
		this.url=url;
		this.driver.get(this.url);
	}	
	
	public String getPageTitle(){
		return this.driver.getTitle();
	}
	
	public boolean isLoaded(){
		System.out.println(this.getPageTitle());
		return LoginPage.TITLE.equals(this.getPageTitle());	
	}
	
	public boolean login(String username,String password){
		this.driver.findElement(By.id(this.USERNAME_TEXT)).sendKeys(username);
		this.driver.findElement(By.id(this.PASSWORD_TEXT)).sendKeys(password);
		this.driver.findElement(By.id(this.LOGIN_BUTTON)).click();
		
		return FirstPage.TITLE.equals(this.getPageTitle());	
	}

}
