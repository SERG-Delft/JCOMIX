import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

class replicate12Test {

	private WebDriver driver;

	@BeforeEach
	void setup() throws Exception {
		System.setProperty("webdriver.chrome.driver", "/home/dimitri/Documents/gitlab/honours/comix/applications/chromedriver/linux/chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8080/sbank/Affilier3DsecureForm.html");
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

	@Test
	public void aTest(){
		String testObjective = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:lu=\"http://commonws/cetrel/lu\"> <soapenv:Header/> <soapenv:Body> <lu:perform> <lu:resInput> <lu:UserName>wgen0001</lu:UserName> <lu:UserName>1 or/**/\"a\"=\"a\" or</lu:UserName> <lu:IssuerBankCode>0111</lu:IssuerBankCode> <lu:RequestId>0001User</lu:RequestId> <lu:CardNumber>123456789123456</lu:CardNumber> </lu:resInput> </lu:perform> </soapenv:Body></soapenv:Envelope>";

		driver.findElement(By.xpath("//input[@name='CardNumber']")).sendKeys("245679912345");
		driver.findElement(By.xpath("//input[@name='BankCode']")).sendKeys("0111");
		driver.findElement(By.xpath("//input[@name='UserName']")).sendKeys("wgen0001</lu:UsRrName> <lu:/serName>- ou\"** a~=\"f\"hom");
		driver.findElement(By.xpath("//input[@name='RequestId']")).sendKeys("0001User");
		driver.findElement(By.xpath("//input[@name='submit']")).submit();

		String actualXML = driver.findElement(By.cssSelector("pre")).getText().replaceAll("\\s+", " ");

		Assertions.assertNotEquals(testObjective, actualXML);
	}
}