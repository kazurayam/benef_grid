import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

int loopCount = 0
int MAX_REPEAT = 3    // exit infinite loop if the loopCount execeded the max

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path mhtml = projectDir.resolve('benef_grid.mhtml')
String url = mhtml.toFile().toURI().toURL().toExternalForm()

WebUI.openBrowser('')
WebUI.navigateToUrl(url)
WebUI.delay(1)

WebDriver driver = DriverFactory.getWebDriver()
String expectedValue = '1er'

Boolean removed  = supprimerARow(driver, expectedValue); loopCount += 1
while (removed && loopCount <= MAX_REPEAT) {
	removed = supprimerARow(driver, expectedValue); loopCount += 1
}

WebUI.closeBrowser()



/**
 * try to find a row with text containing the expected value, and "click" amd "remove" it.
 * if you want to remove multiple rows in the table, call this function mutile times until
 * it returns 0.
 * 
 * @param driver instance of WebDriver
 * @param String the expected value. e.g. '2nd'
 * @return true if a row was found and "clicked"; false in case no row was found
 */
Boolean supprimerARow(WebDriver driver, String pattern) {
	println "***************\n supprimerARow(driver, pattern=${pattern}) was inveked"
	
	'the conteneur table'
	WebElement benef_grid = driver.findElement(By.xpath('//div[@class="conteneur"]/table/tbody'))

	'capture all the rows available in the table '
	List<WebElement> rows = benef_grid.findElements(By.tagName('tr'))
	WebUI.comment("Number of rows: ${rows.size()}")
	
	if (rows.size() > 0) {
		for (int rx = 0; rx < rows.size(); rx++) {
			List<WebElement> cols = rows[rx].findElements(By.tagName('td'))
			Boolean matchFound = false
			for (int cx = 1; cx < cols.size(); cx++) {
				if (cols[cx].getText().equalsIgnoreCase(pattern)) {
					matchFound = true
				}
				println "cell(${rx},${cx})=${cols[cx].getText()}, matchFound=${matchFound}"
				if (matchFound) {
					break
				}
			}
			'this row contains a text matching with the given pattern'
			if (matchFound) {
				'click the check box of the row'
				cols[0].click()
				println "Yah! I clicked the button to removed this row ------ the row is should be removed by JavaScript"
				
				'ensure the page gets stable'
				WebUI.delay(1)
				'1 row was found and processed'
				return true
			} else {
				;    'continue to process the following row'	
			}
		}
		'We found no row which contains text matching with the given pattern'
		return false
		
	} else {
		'zero row found'
		return false
	}
}

