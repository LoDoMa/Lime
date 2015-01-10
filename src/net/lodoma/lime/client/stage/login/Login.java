package net.lodoma.lime.client.stage.login;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.menu.MainMenuPopulator;
import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonListener;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.simple.SimpleButton;
import net.lodoma.lime.gui.simple.SimpleTextField;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Vector2;

public class Login extends Stage
{
    private class LoginListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            String username = usernameField.getText();
            
            String passwordstr = passwordField.getText();
            @SuppressWarnings("unused")
            long password = HashHelper.hash64(passwordstr);
            
            // TODO: verify
            
            Credentials credentials = new Credentials(username);
            
            new Menu(manager, new MainMenuPopulator(credentials)).startStage();
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private GUIContainer container;

    private SimpleTextField usernameField;
    private SimpleTextField passwordField;
    
    public Login(StageManager manager)
    {
        super(manager);
    }
    
    @Override
    public void preStart()
    {
        container = new GUIContainer();
        
        container.removeAll();
        
        usernameField = new SimpleTextField(new Rectangle(0.25f, 0.50f, 0.5f, 0.05f), "username");
        passwordField = new SimpleTextField(new Rectangle(0.25f, 0.44f, 0.5f, 0.05f), "password");
        
        container.addElement(usernameField);
        container.addElement(passwordField);
        container.addElement(new SimpleButton(new Rectangle(0.25f, 0.38f, 0.5f, 0.05f), new LoginListener(), "Login"));
    }
    
    @Override
    public void onStart()
    {
        
    }
    
    @Override
    public void onEnd()
    {
        
    }
    
    @Override
    public void postEnd()
    {
        
    }
    
    @Override
    public void update(double timeDelta)
    {
        Input.update();
        container.update(timeDelta);
    }
    
    @Override
    public void render()
    {
        Program.menuProgram.useProgram();
        Program.menuProgram.setUniform("texture", UniformType.INT1, 0);
        
        container.render();
    }
}
