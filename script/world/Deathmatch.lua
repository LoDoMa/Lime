
local C = lime.module("Deathmatch/C")

local attribEntityPosX = C.attribEntityPosX
local attribEntityPosY = C.attribEntityPosY
local attribEntityParent = C.attribEntityParent
local attribLykkeFocusX = C.attribLykkeFocusX
local attribLykkeFocusY = C.attribLykkeFocusY

local cWorldGravityX = C.cWorldGravityX
local cWorldGravityY = C.cWorldGravityY
local cWorldAmbientLightR = C.cWorldAmbientLightR
local cWorldAmbientLightG = C.cWorldAmbientLightG
local cWorldAmbientLightB = C.cWorldAmbientLightB
local cCameraPadding = C.cCameraPadding
local cCameraScaleX = C.cCameraScaleX
local cCameraScaleY = C.cCameraScaleY

local Camera = lime.module("common/Camera")
Camera.setPadding(cCameraPadding)
Camera.setMinimumScale(cCameraScaleX, cCameraScaleY)

local World = lime.module("Deathmatch/World")
local Material = lime.module("Deathmatch/Material")

-- Timers/countdowns
local pickupCountdownMax = 15
local pickupCountdown = pickupCountdownMax

local connectedUsers = {}
local playerIDs = {}

function Lime_WorldInit()
    lime.setAmbientLight(cWorldAmbientLightR, cWorldAmbientLightG, cWorldAmbientLightB)
    lime.setWorldGravity(cWorldGravityX, cWorldGravityY)
end

local function createPlayer(userID)
    local playerID = lime.newEntity()
    lime.setAttribute(playerID, attribEntityPosX, 1.5)
    lime.setAttribute(playerID, attribEntityPosY, 1.5)
    lime.setAttribute(playerID, attribEntityParent, userID)
    lime.assignScript(playerID, "Deathmatch/Lykke")
    return playerID
end

local function onJoin(userID)
    local playerID = createPlayer(userID)
    connectedUsers[userID] = playerID
end

local function onLeave(userID)
    local playerID = connectedUsers[userID]
    connectedUsers[userID] = nil

    lime.removeEntity(playerID)
end

local function spawnSegment(x, y)
    World.addLight(x + 1.5,  y + 1.5,  14.0, 0.05, 1.0,  0.4)
    World.addLight(x + 18.5, y + 8,    14.0, 1.0,  0.4,  0.05)

    World.addTerrain("stone", x + 0,    y + 0,    x + 0,    y - 2,    x + 8.5,  y - 2) -- bottom
    World.addTerrain("stone", x + 0,    y + 0,    x + 8.5,  y - 2,    x + 8.5,  y + 0) -- bottom
    World.addTerrain("stone", x + 11.5, y + 0,    x + 11.5, y - 2,    x + 20,   y - 2) -- bottom
    World.addTerrain("stone", x + 11.5, y + 0,    x + 20,   y - 2,    x + 20,   y + 0) -- bottom
    World.addTerrain("stone", x - 2,    y + 0,    x + 0,    y - 2,    x + 0,    y + 0) -- bottom left corner
    World.addTerrain("stone", x + 20,   y + 0,    x + 20,   y - 2,    x + 22,   y + 0) -- bottom right corner
    World.addTerrain("stone", x - 2,    y + 9.5,  x - 2,    y + 0,    x + 0,    y + 0) -- left
    World.addTerrain("stone", x - 2,    y + 9.5,  x + 0,    y + 0,    x + 0,    y + 9.5) -- left
    World.addTerrain("stone", x + 20,   y + 9.5,  x + 20,   y + 0,    x + 22,   y + 0) -- right
    World.addTerrain("stone", x + 20,   y + 9.5,  x + 22,   y + 0,    x + 22,   y + 9.5) -- right
    World.addTerrain("stone", x + 0,    y + 11.5, x + 0,    y + 9.5,  x + 8.5,  y + 9.5) -- top
    World.addTerrain("stone", x + 0,    y + 11.5, x + 8.5,  y + 9.5,  x + 8.5,  y + 11.5) -- top
    World.addTerrain("stone", x + 11.5, y + 11.5, x + 11.5, y + 9.5,  x + 20,   y + 9.5) -- top
    World.addTerrain("stone", x + 11.5, y + 11.5, x + 20,   y + 9.5,  x + 20,   y + 11.5) -- top
    World.addTerrain("stone", x - 2,    y + 9.5,  x + 0,    y + 9.5,  x + 0,    y + 11.5) -- top left corner
    World.addTerrain("stone", x + 20,   y + 11.5, x + 20,   y + 9.5,  x + 22,   y + 9.5) -- top right corner

    World.addTerrain("stone", x + 3,    y + 4,    x + 3,    y + 3,    x + 9.5,  y + 3) -- obstacle bottom-left
    World.addTerrain("stone", x + 3,    y + 4,    x + 9.5,  y + 3,    x + 9.5,  y + 4) -- obstacle bottom-left
    World.addTerrain("stone", x + 10.5, y + 4,    x + 10.5, y + 3,    x + 17,   y + 3) -- obstacle bottom-right
    World.addTerrain("stone", x + 10.5, y + 4,    x + 17,   y + 3,    x + 17,   y + 4) -- obstacle bottom-right
    World.addTerrain("stone", x + 9.5,  y + 6.5,  x + 9.5,  y + 3,    x + 10.5, y + 3) -- obstacle middle
    World.addTerrain("stone", x + 9.5,  y + 6.5,  x + 10.5, y + 3,    x + 10.5, y + 6.5) -- obstacle middle
    World.addTerrain("stone", x + 3,    y + 6.5,  x + 3,    y + 5.5,  x + 9.5,  y + 5.5) -- obstacle top-left
    World.addTerrain("stone", x + 3,    y + 6.5,  x + 9.5,  y + 5.5,  x + 9.5,  y + 6.5) -- obstacle top-left
    World.addTerrain("stone", x + 10.5, y + 6.5,  x + 10.5, y + 5.5,  x + 17,   y + 5.5) -- obstacle top-right
    World.addTerrain("stone", x + 10.5, y + 6.5,  x + 17,   y + 5.5,  x + 17,   y + 6.5) -- obstacle top-right

    World.addPickupLocation(x + 1.5,  y + 1.5)
    World.addPickupLocation(x + 18.5, y + 1.5)
    World.addPickupLocation(x + 4,    y + 1.5)
    World.addPickupLocation(x + 16,   y + 1.5)
    World.addPickupLocation(x + 4,    y + 8)
    World.addPickupLocation(x + 10,   y + 8)
    World.addPickupLocation(x + 16,   y + 8)
end

function Lime_Init()
    lime.addEventListener("Lime::OnJoin", onJoin)
    lime.addEventListener("Lime::OnLeave", onLeave)

    Material.addMaterial("stone", 1.0, 1.0, 1.0, 5, 0.7, 0.0)

    spawnSegment(0, 0)
    spawnSegment(0, 13.5)
    spawnSegment(0, 27)

    print("Gamemode initialized")
end

local function spawnPickup()
    local pickupX, pickupY = World.getPickupLocation()

    local pickupID = lime.newEntity()
    lime.setAttribute(pickupID, "posx", pickupX)
    lime.setAttribute(pickupID, "posy", pickupY)
    lime.setAttribute(pickupID, "applyEffects", function(playerID)
        print("Picked up by player [ID=" .. playerID .. "]")
    end)
    lime.assignScript(pickupID, "Deathmatch/Pickup")
end

local function updateTimers(timeDelta)
    if pickupCountdown > 0 then
        pickupCountdown = pickupCountdown - timeDelta
        if pickupCountdown < 0 then
            pickupCountdown = pickupCountdownMax
            spawnPickup();
        end
    end
end

function Lime_Update(timeDelta)
    updateTimers(timeDelta)
end

function Lime_PostUpdate()
    for user, playerID in pairs(connectedUsers) do
        local focusX = lime.getAttribute(playerID, attribLykkeFocusX)
        local focusY = lime.getAttribute(playerID, attribLykkeFocusY)
        Camera.addFocusPoint(focusX, focusY)
    end

    Camera.update()

    local translationX, translationY = Camera.getTranslation()
    local scaleX, scaleY = Camera.getScale()

    for userID in pairs(connectedUsers) do
        lime.setCameraTranslation(userID, translationX, translationY)
        lime.setCameraScale(userID, scaleX, scaleY)
    end
end

function Lime_Clean()

end
