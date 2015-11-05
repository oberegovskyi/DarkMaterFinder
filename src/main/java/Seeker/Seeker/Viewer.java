package Seeker.Seeker;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Viewer {
	private static final String gameUrl = "http://ru.ogame.gameforge.com/";
	private static WebDriver driver = new ChromeDriver();
	private static final String resultFile = "result.txt";
	private static final int step = 80;
	private String login;
	private String password;
	private String universe;

	public Viewer(String login, String password, String universe) {
		this.login = login;
		this.password = password;
		this.universe = universe;
	}

	public void start() {
		driver.navigate().to(gameUrl);
		openLoginPanel();
		login(login, password, universe);
		goToGala();
		for (int gal = 9; gal >= 1; gal--) {
			int ss = 499;
			Boolean foundDM = false;
			while (ss >= 1) {
				goToKor(gal, ss);
				if (isDarkMater(foundDM)) {
					foundDM = true;
					Helper.writeToFile(prepareToWrite(gal, ss), resultFile);
					System.out.println(gal + ":" + ss);
				}
				if (foundDM) {
					ss -= step;
				} else {
					ss--;
				}
			}
		}
	}

	private void openLoginPanel() {
		WebElement logPanel = driver.findElement(By.id("loginBtn"));
		logPanel.click();
	}

	private void login(String login, String password, String universe) {
		Select uni = new Select(driver.findElement(By.id("serverLogin")));
		uni.selectByVisibleText(universe);
		WebElement log = driver.findElement(By.id("usernameLogin"));
		log.sendKeys(login);
		WebElement pass = driver.findElement(By.id("passwordLogin"));
		pass.sendKeys(password);
		WebElement logButton = driver.findElement(By.id("loginSubmit"));
		logButton.click();
	}

	private void goToGala() {
		WebElement gala = driver.findElement(By
				.xpath("//span[contains(.,'Галактика')]"));
		gala.click();
	}

	private void goToKor(Integer gal, Integer ss) {
		WebElement galElement = driver.findElement(By.id("galaxy_input"));
		galElement.sendKeys(String.valueOf(gal));
		WebElement ssElement = driver.findElement(By.id("system_input"));
		ssElement.sendKeys(String.valueOf(ss));
		WebElement goButton = driver
				.findElement(By.cssSelector("div.btn_blue"));
		goButton.click();
		try {
			WebDriverWait wait = new WebDriverWait(driver, 3);
			String element_xpath = ".//*[@id='galaxyLoading' and not(contains(@style,'display: none'))]";
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath(element_xpath)));
			wait = new WebDriverWait(driver, 3);
			element_xpath = ".//*[@id='galaxyLoading' and contains(@style,'display: none')]";
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath(element_xpath)));
		} catch (TimeoutException ex) {
			Helper.writeToFile(prepareToWrite(gal, ss) + ":Error showing",
					resultFile);
		}
	}

	private Boolean isDarkMater(Boolean flag) {
		try {
			if (flag) {
				driver.findElement(By.cssSelector("img.float_left"));
			} else {
				driver.findElement(By.id("debris17"));
			}
			return true;
		} catch (NoSuchElementException ex) {
			return false;
		}
	}

	private String prepareToWrite(Integer gal, Integer ss) {
		return gal + ":" + ss;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUniverse() {
		return universe;
	}

	public void setUniverse(String universe) {
		this.universe = universe;
	}
}
