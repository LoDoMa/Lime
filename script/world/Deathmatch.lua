
local World = lime.include("World")
local Material = lime.include("Material")

-- Constants
local cameraRatioX = 16
local cameraRatioY = 9
local cameraRatio = cameraRatioX / cameraRatioY
local cameraPadding = 4 -- minimum space around edge players
local minCameraWidth = 8
local minCameraHeight = 4.5

-- Timers/countdowns
local pickupCountdownMax = 15
local pickupCountdown = pickupCountdownMax

local connectedUsers = {}
local playerIDs = {}

function Lime_WorldInit()
    lime.setAmbientLight(0.01, 0.0, 0.1)
    lime.setWorldGravity(0.0, -18.0)
end

local function createPlayer(userID)
    local playerID = lime.newEntity()
    lime.setAttribute(playerID, "owner", userID)
    lime.assignScript(playerID, "Player")
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
    lime.assignScript(pickupID, "Pickup")
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
    local cameraBoundsMinX = nil
    local cameraBoundsMaxX = nil
    local cameraBoundsMinY = nil
    local cameraBoundsMaxY = nil

    for user, playerID in pairs(connectedUsers) do
        local focusX = lime.getAttribute(playerID, "focusX")
        local focusY = lime.getAttribute(playerID, "focusY")

        if cameraBoundsMinX then
            cameraBoundsMinX = math.min(cameraBoundsMinX, focusX)
            cameraBoundsMaxX = math.max(cameraBoundsMaxX, focusX)
            cameraBoundsMinY = math.min(cameraBoundsMinY, focusY)
            cameraBoundsMaxY = math.max(cameraBoundsMaxY, focusY)
        else
            cameraBoundsMinX = focusX
            cameraBoundsMaxX = focusX
            cameraBoundsMinY = focusY
            cameraBoundsMaxY = focusY
        end
    end

    if cameraBoundsMinX then
        cameraBoundsMinX = cameraBoundsMinX - cameraPadding
        cameraBoundsMaxX = cameraBoundsMaxX + cameraPadding
        cameraBoundsMinY = cameraBoundsMinY - cameraPadding
        cameraBoundsMaxY = cameraBoundsMaxY + cameraPadding

        local cameraBoundsWidth = cameraBoundsMaxX - cameraBoundsMinX
        local cameraBoundsHeight = cameraBoundsMaxY - cameraBoundsMinY

        local cameraCenterX = cameraBoundsMaxX - cameraBoundsWidth / 2.0
        local cameraCenterY = cameraBoundsMaxY - cameraBoundsHeight / 2.0

        if cameraBoundsWidth < minCameraWidth then cameraBoundsWidth = minCameraWidth end
        if cameraBoundsHeight < minCameraHeight then cameraBoundsHeight = minCameraHeight end

        local currentRatio = cameraBoundsWidth / cameraBoundsHeight
        if currentRatio > cameraRatio then
            cameraBoundsHeight = cameraBoundsWidth / cameraRatio
        elseif currentRatio < cameraRatio then
            cameraBoundsWidth = cameraBoundsHeight * cameraRatio
        end

        local cameraTranslationX = cameraCenterX - cameraBoundsWidth / 2.0
        local cameraTranslationY = cameraCenterY - cameraBoundsHeight / 2.0

        for userID in pairs(connectedUsers) do
            lime.setCameraTranslation(userID, cameraTranslationX, cameraTranslationY)
            lime.setCameraRotation(userID, 45)
            lime.setCameraScale(userID, cameraBoundsWidth, cameraBoundsHeight)
        end
    end
end

function Lime_Clean()

end
