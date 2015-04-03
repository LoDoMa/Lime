
--[[

Fields in  gun table:
    [number] maxBulletCount - the maximum amount of bullets that can be available
    [number] maxReloadTime - total reloading duration
    [number] maxNextTime - minimum time between shots
    [number] bulletCount - current bullet count
    [number] reloadAmount - amount of bullets that can be reloaded at a time
    
    Used internally:
    [boolean] allowShooting - used internally, true if shooting is allowed
    [number] reloadTime - remaining time required to reload
    [number] nextTime - remaining time required to allow another shot

]]


-- Creates a new gun table
local function create()
    return {
        allowShooting = false,
        maxBulletCount = 10,
        maxReloadTime = 1,
        maxNextTime = 0.2,
        bulletCount = 10,
        reloadTime = 1,
        reloadAmount = 5,
        nextTime = 0.1,
    }
end

-- Updates the timers, determines if shooting is allowed
local function update(gun, timeDelta)
    if gun.reloadTime > 0 then
        gun.reloadTime = gun.reloadTime - timeDelta
    end
    if gun.reloadTime < 0 then
        gun.reloadTime = gun.maxReloadTime
        gun.bulletCount = gun.bulletCount + gun.reloadAmount
        if gun.bulletCount > gun.maxBulletCount then
            gun.bulletCount = gun.maxBulletCount
        end
    end

    if gun.nextTime > 0 then
        gun.allowShooting = false
        gun.nextTime = gun.nextTime - timeDelta
    end
    if gun.nextTime <= 0 then
        gun.nextTime = 0
        if gun.bulletCount > 0 then
            gun.allowShooting = true
        end
    end
end

-- Returns true if the gun can successfully shoot.
-- If shooting is allowed, it also lowers the bullet count and resets timers.
local function shoot(gun)
    if gun.allowShooting then
        gun.bulletCount = gun.bulletCount - 1
        gun.nextTime = gun.maxNextTime
        gun.reloadTime = gun.maxReloadTime
        return true
    end
    return false
end

__LIME_MODULE_TABLE__ = {
    create = create,
    update = update,
    shoot = shoot,
}