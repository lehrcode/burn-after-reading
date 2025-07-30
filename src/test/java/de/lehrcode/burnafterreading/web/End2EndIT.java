package de.lehrcode.burnafterreading.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.util.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class End2EndIT {
    // Shared between all tests in this class.
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create(new Playwright.CreateOptions()
                                           .setEnv(Collections.singletonMap("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1")));
        browser = playwright.chromium()
                            .launch(new BrowserType.LaunchOptions().setHeadless(false)
                                                                   .setSlowMo(1000)
                                                                   .setChannel("chrome"));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void storeAndReadMessage() throws InterruptedException {
        var inputText = "Secret\nmessage";
        page.navigate("http://localhost:8080/");
        page.locator("textarea").fill(inputText);
        page.locator("form button").click();
        var createdLink = page.locator("body > main > div > a").innerText();
        System.out.println(createdLink);
        page.navigate(createdLink);
        page.locator("div button").click();
        var outputText = page.locator("textarea").inputValue();
        assertEquals(inputText, outputText);
    }
}
