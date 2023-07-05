package ua.prom.roboticsdmc.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.view.ViewProvider;


@ExtendWith(value = { MockitoExtension.class })
class FrontControllerTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private CourseDao courseDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private ViewProvider viewProvider;

    @InjectMocks
    private FrontController frontController;
    
//    @Test
//    void run_shouldReturnMessage_whenChoseIsUnexpected() {
//        
//        String text = tapSystemOut(() -> {
//            print("Hello Baeldung Readers!!");
//        });
//
//        Assert.assertEquals("Hello Baeldung Readers!!", text.trim());
//        
//        int unexpetedChoice = 1000;
//        
//        doNothing().when(frontController).run();
//        frontController.run();
//        verify(frontController).run();
//    }
}
