package ru.netology.testing.uiautomator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.thread


const val SETTINGS_PACKAGE = "com.android.settings"
const val MODEL_PACKAGE = "ru.netology.testing.uiautomator"

const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class ChangeTextNewTest {

 private lateinit var device: UiDevice
 //private val originalText = "Привет, UiAutomator!"
 private val textToSet = "My first test"
 private val textToSet2 = ""


 private fun waitForPackage(packageName: String) {
  val context = ApplicationProvider.getApplicationContext<Context>()
  val intent = context.packageManager.getLaunchIntentForPackage(packageName)
  context.startActivity(intent)
  device.wait(Until.hasObject(By.pkg(packageName)), TIMEOUT)
 }

 @Before
 fun beforeEachTest() {
  // Press home
  device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
  device.pressHome()

  // Wait for launcher
  val launcherPackage = device.launcherPackageName
  device.wait(Until.hasObject(By.pkg(launcherPackage)), TIMEOUT)
 }

 @Test
 fun testInternetSettings() {
  waitForPackage(SETTINGS_PACKAGE)

  device.findObject(
   UiSelector().resourceId("android:id/title").instance(0)
  ).click()
 }

 @Test
 fun testChangeText() {
  val packageName = MODEL_PACKAGE
  waitForPackage(packageName)

  device.findObject(By.res(packageName, "userInput")).text = textToSet
  device.findObject(By.res(packageName, "buttonChange")).click()

  val result = device.findObject(By.res(packageName, "textToBeChanged")).text
  assertEquals(result, textToSet)
 }

 @Test
 fun testInputEmptyText() {
  val packageName = MODEL_PACKAGE
  waitForPackage(packageName)

  val originalText = "Привет, UiAutomator!"

  device.findObject(By.res(packageName, "userInput")).text = textToSet2
  device.findObject(By.res(packageName, "buttonChange")).click()

  val result = device.findObject(By.res(packageName, "textToBeChanged")).text
  assertEquals(result, originalText)
 }

 @Test
 fun testInNewInputActivity() {
  val packageName = MODEL_PACKAGE
  waitForPackage(packageName)

  device.findObject(By.res(packageName, "userInput")).text = textToSet
  device.findObject(By.res(packageName, "buttonActivity")).click()

  Thread.sleep(5000)

  val result = device.findObject(By.res(packageName, "text")).text
  assertEquals(result, textToSet)
 }
}