package es.usc.citius.zebrafish.modelo.usuarios;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserHelperTest {
    @Test
    public void testUuid() {
        Assert.assertNotNull(UserHelper.uuid());
    }

}
