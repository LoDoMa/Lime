package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;

/**
 * ClientLogic.
 * TODO: improve
 * 
 * @author Lovro Kalinovčić
 */
public interface ClientLogic
{
    /**
     * The first initialization stage.
     * The client is passed to the implementation,
     * the implementation should do basic initialization.
     * 
     * @param client - the client that uses this logic
     */
    public void baseInit(Client client);
    
    /**
     * The second initialization stage.
     * The implementation should set new client properties.
     */
    public void propertyInit();
    
    /**
     * The third initialization stage.
     * The implementation should get and store client properties.
     */
    public void fetchInit();
    
    /**
     * The fourth initialization stage.
     * The implementation should finish its initialization.
     */
    public void generalInit();
    
    /**
     * Closes and frees resources.
     */
    public void clean();
    
    /**
     * Logic cycle.
     */
    public void logic();
}
