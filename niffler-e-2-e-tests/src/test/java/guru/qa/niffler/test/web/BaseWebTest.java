package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class BaseWebTest {
    protected static final Config CFG = Config.getInstance();
}
