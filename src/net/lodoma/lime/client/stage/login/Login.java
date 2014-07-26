package net.lodoma.lime.client.stage.login;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.client.stage.menu.MenuTextField;
import net.lodoma.lime.client.stage.menu.populator.MainMenuPopulator;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.HashHelper;

public class Login extends Stage
{
    private GUIContainer container;
    
    public Login(StageManager manager)
    {
        super(manager);
    }
    
    @Override
    public void preStart()
    {
        container = new GUIContainer();
        
        container.removeAll();

        final StageManager stageManager = this.manager;
        final MenuTextField username = new MenuTextField(new Rectangle(0.25f, 0.50f, 0.5f, 0.05f), "username");
        final MenuTextField password = new MenuTextField(new Rectangle(0.25f, 0.44f, 0.5f, 0.05f), "password", '+');
        
        container.addElement(username);
        container.addElement(password);
        container.addElement(new MenuButton(new Rectangle(0.25f, 0.38f, 0.5f, 0.05f), "Login", new Runnable()
        {
            private MenuTextField usernameTextField = username;
            private MenuTextField passwordTextField = password;
            
            @Override
            public void run()
            {
                String username = usernameTextField.getText();
                
                String passwordstr = passwordTextField.getText();
                @SuppressWarnings("unused")
                long password = HashHelper.hash64(passwordstr);
                
                // TODO: verify
                
                Credentials credentials = new Credentials(username);
                
                new Menu(stageManager, new MainMenuPopulator(credentials)).startStage();
            }
        }));
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
        container.update(timeDelta);
        Input.update();
    }
    
    @Override
    public void render()
    {
        container.render();
    }
}
