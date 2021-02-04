import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path mhtml = projectDir.resolve('benef_grid.mhtml')
String url = mhtml.toFile().toURI().toURL().toExternalForm()

WebUI.openBrowser('')
WebUI.navigateToUrl('file:///Users/kazuakiurayama/Downloads/benef_grid.mhtml')

WebDriver driver = DriverFactory.getWebDriver()

String ExpectedValue = '2nd'
boolean isWorking = true;
boolean hasRemoved = false;

while (isWorking) {
	'In Table'
	WebElement benef_grid = driver.findElement(By.xpath('//div[@class="conteneur"]/table/tbody'))
	
	'To locate rows of table it will Capture all the rows available in the table '
	List<WebElement> Rows = benef_grid.findElements(By.tagName('tr'))
	
	println('Nbr of rows: ' + Rows.size())
	if (Rows.size() <= 1) {
		isWorking = false;
	} else {
	
		'Find a matching text in a table and performing action'
		Loop:
		for (int i = 0; i < Rows.size(); i++) {
			
			'To locate columns(cells) of that specific row'
			List<WebElement> Cols = Rows.get(i).findElements(By.tagName('td'))
		
			for (int j = 0; j < Cols.size(); j++) {
				'Verifying the expected text in the each cell'
				if (Cols.get(j).getText().equalsIgnoreCase(ExpectedValue)) {
					
					'Click the check box of that row then delete the row'
					Cols.get(0).click();
					
					hasRemoved = true;
					
					WebUI.click(findTestObject('Sinistres/input_supprimer'))
					break Loop;
				}
			}
		}
		if (!hasRemoved) {
			isWorking = false;
		}
	}
}

WebUI.closeBrowser()