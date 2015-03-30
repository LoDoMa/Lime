
local World = lime.include("World")
local Material = lime.include("Material")

-- Constants
local cameraRatioX = 16
local cameraRatioY = 9
local cameraRatio = cameraRatioX / cameraRatioY
local cameraPadding = 4 -- minimum space around edge players
local minCameraWidth = 32
local minCameraHeight = 18

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

function Lime_Init()
    lime.addEventListener("Lime::OnJoin", onJoin)
    lime.addEventListener("Lime::OnLeave", onLeave)

    Material.addMaterial("stone", 5, 0.4, 0.05)

    World.addPickupLocation(1.5, 1.5)
    World.addPickupLocation(18.5, 1.5)
    World.addPickupLocation(4, 1.5)
    World.addPickupLocation(10, 1.5)
    World.addPickupLocation(16, 1.5)
    World.addPickupLocation(4, 8)
    World.addPickupLocation(10, 8)
    World.addPickupLocation(16, 8)

    World.addLight(1.5, 1.5, 14, 0.05, 1.0, 0.4)
    World.addLight(18.5, 8, 14, 1.0, 0.4, 0.05)

    World.addTerrain("stone", 0, 0, 0, -2, 20, -2) -- bottom
    World.addTerrain("stone", 0, 0, 20, -2, 20, 0) -- bottom
    World.addTerrain("stone", -2, 0, 0, -2, 0, 0) -- bottom left corner
    World.addTerrain("stone", 20, 0, 20, -2, 22, 0) -- bottom right corner
    World.addTerrain("stone", -2, 9.5, -2, 0, 0, 0) -- left
    World.addTerrain("stone", -2, 9.5, 0, 0, 0, 9.5) -- left
    World.addTerrain("stone", 20, 9.5, 20, 0, 22, 0) -- right
    World.addTerrain("stone", 20, 9.5, 22, 0, 22, 9.5) -- right
    World.addTerrain("stone", 0, 11.5, 0, 9.5, 20, 9.5) -- top
    World.addTerrain("stone", 0, 11.5, 20, 9.5, 20, 11.5) -- top
    World.addTerrain("stone", -2, 9.5, 0, 9.5, 0, 11.5) -- top left corner
    World.addTerrain("stone", 20, 11.5, 20, 9.5, 22, 9.5) -- top right corner

    World.addTerrain("stone", 3, 4, 3, 3, 9.5, 3) -- obstacle bottom-left
    World.addTerrain("stone", 3, 4, 9.5, 3, 9.5, 4) -- obstacle bottom-left
    World.addTerrain("stone", 10.5, 4, 10.5, 3, 17, 3) -- obstacle bottom-right
    World.addTerrain("stone", 10.5, 4, 17, 3, 17, 4) -- obstacle bottom-right
    World.addTerrain("stone", 9.5, 6.5, 9.5, 3, 10.5, 3) -- obstacle middle
    World.addTerrain("stone", 9.5, 6.5, 10.5, 3, 10.5, 6.5) -- obstacle middle
    World.addTerrain("stone", 3, 6.5, 3, 5.5, 9.5, 5.5) -- obstacle top-left
    World.addTerrain("stone", 3, 6.5, 9.5, 5.5, 9.5, 6.5) -- obstacle top-left
    World.addTerrain("stone", 10.5, 6.5, 10.5, 5.5, 17, 5.5) -- obstacle top-right
    World.addTerrain("stone", 10.5, 6.5, 17, 5.5, 17, 6.5) -- obstacle top-right


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
