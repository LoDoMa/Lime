package net.lodoma.lime.security;

public class Credentials
{
    private final String username;
    
    public Credentials(String username)
    {
        this.username = username;
    }
    
    public String getUsername()
    {
        return username;
    }
}
