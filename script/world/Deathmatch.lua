
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
    lime.setAttribute(playerID, attribEntityPosX, 0)
    lime.setAttribute(playerID, attribEntityPosY, 4 * 1.5)
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

local function createWorld()
    local scaleX = 1.25
    local scaleY = 1.5

    local addScaled = function(x1, y1, x2, y2, x3, y3)
        World.addTerrain("stone", x1 * scaleX, y1 * scaleY, x2 * scaleX, y2 * scaleY, x3 * scaleX, y3 * scaleY)
    end

    local addLightScaled = function(x, y, rad, r, g, b)
        World.addLight(x * scaleX, y * scaleY, rad, r, g, b)
    end

    local addBox = function(x, y, rx, ry, s)
        local boxID = lime.newComponent("dynamic", x * scaleX + rx, y * scaleY + ry, 0.0)
        lime.selectComponent(boxID)

        local shapeID = lime.newShape("triangle-group")
        lime.selectShape(shapeID)
        lime.addShapeTriangle(0, 0, s, 0, s, s)
        lime.addShapeTriangle(0, 0, 0, s, s, s)
        lime.setShapeDensity(5.0)
        lime.setShapeFriction(0.5)
        lime.setShapeRestitution(0.0)
        lime.setShapeColor(1.0, 1.0, 1.0, 1.0)
        lime.setShapeTexture("gamemode/Deathmatch/Box")
        lime.setShapeTexturePoint(0, 0)
        lime.setShapeTextureSize(s, s)
        lime.updateShape()
    end

    local addLightpost = function(x, y, s)
        local lightpostID = lime.newComponent("static", x * scaleX, y * scaleY, 0.0)
        lime.selectComponent(lightpostID)

        local shapeID = lime.newShape("circle")
        lime.selectShape(shapeID)
        lime.setShapeDensity(0)
        lime.setShapeFriction(0)
        lime.setShapeRestitution(0)
        lime.setShapeColor(1.0, 1.0, 1.0, 1.0)
        lime.setShapeAnimation("gamemode/Deathmatch/Lightpost")
        lime.setShapeAnimationSelection("def")
        lime.setShapeAnimationScale(s * scaleX, s * scaleY)
        lime.setShapeRadius(0)
        lime.updateShape()

        addLightScaled(x + s * 0.205, y + s * 0.815, 1.5 * s, 1.0, 0.6, 0.0)
        addLightScaled(x - s * 0.33, y + s * 0.8, 1.5 * s, 1.0, 0.6, 0.0)

        local topID = lime.newComponent("static", x * scaleX, y * scaleY, 0.0)
        lime.selectComponent(topID)

        shapeID = lime.newShape("triangle-group")
        lime.selectShape(shapeID)
        lime.setShapeDensity(0.0)
        lime.setShapeFriction(0.0)
        lime.setShapeRestitution(0.0)
        lime.addShapeTriangle(-0.37 * s * scaleX, s * 0.82 * scaleY, -0.37 * s * scaleX, s * 0.85 * scaleY, 0.26 * s * scaleX, s * 0.85 * scaleY)
        lime.addShapeTriangle(-0.37 * s * scaleX, s * 0.82 * scaleY, 0.26 * s * scaleX, s * 0.82 * scaleY, 0.26 * s * scaleX, s * 0.85 * scaleY)
        lime.updateShape()
    end

    addLightScaled(0, 1, 14, 1, 0.4, 0.2)
    addLightScaled(0, 12, 14, 0.4, 0.2, 1.0)

    addBox(-1.2, 0, 0, 0, 0.8)
    addBox(-1.2, 0, 0.8, 0, 0.8)
    addBox(-1.2, 0, 1.6, 0, 0.8)
    addBox(-1.2, 0, 0.4, 0.8, 0.8)
    addBox(-1.2, 0, 1.2, 0.8, 0.8)
    addBox(-1.2, 0, 0.8, 1.6, 0.8)

    addBox(-3.7, 19, 0, 0, 0.8)
    addBox(-3.7, 19, 0.8, 0, 0.8)
    addBox(-3.7, 19, 1.6, 0, 0.8)
    addBox(-3.7, 19, 0.4, 0.8, 0.8)
    addBox(-3.7, 19, 1.2, 0.8, 0.8)
    addBox(-3.7, 19, 0.8, 1.6, 0.8)

    addBox(1.3, 19, 0, 0, 0.8)
    addBox(1.3, 19, 0.8, 0, 0.8)
    addBox(1.3, 19, 1.6, 0, 0.8)
    addBox(1.3, 19, 0.4, 0.8, 0.8)
    addBox(1.3, 19, 1.2, 0.8, 0.8)
    addBox(1.3, 19, 0.8, 1.6, 0.8)

    addLightpost(0, 3, 3)
    addLightpost(-10.5, 8, 3)
    addLightpost(10.5, 1, 3)
    addLightpost(-5.5, 19, 3)
    addLightpost(-1.8, 19, 3)
    addLightpost(1.8, 19, 3)
    addLightpost(5.5, 19, 3)

    addScaled(5, 0, -5, 0, -4, -2)
    addScaled(-4, -2, 5, 0, 4, -2)
    addScaled(-4, -2, 4, -2, 0, -3)

    addScaled(-1, 2, 1, 2, 2, 3)
    addScaled(-1, 2, -2, 3, 2, 3)
    addScaled(-8, 2, -11, 2, -10, 1)
    addScaled(-10, 1, -9, 1, -8, 2)
    addScaled(8, 1, 11, 1, 10, 0)
    addScaled(10, 0, 9, 0, 8, 1)
    addScaled(-8, 8, -11, 8, -10, 7)
    addScaled(-10, 7, -9, 7, -8, 8)
    addScaled(8, 7.5, 11, 7.5, 10, 6.5)
    addScaled(10, 6.5, 9, 6.5, 8, 7.5)
    addScaled(0, 10, -1, 11, 1, 11)

    addScaled(-7, 5, -5, 5, -6, 4)
    addScaled(-6, 4, -5, 5, -4, 4)
    addScaled(-5, 5, -4, 4, -4, 6)
    addScaled(-3, 6, -4, 4, -4, 6)
    addScaled(-4, 6, -1, 6, 0, 7)
    addScaled(-4, 6, -4, 8, 0, 7)
    addScaled(0, 8, -4, 8, 0, 7)
    addScaled(7, 5, 5, 5, 6, 4)
    addScaled(6, 4, 5, 5, 4, 4)
    addScaled(5, 5, 4, 4, 4, 6)
    addScaled(3, 6, 4, 4, 4, 6)
    addScaled(4, 6, 1, 6, 0, 7)
    addScaled(4, 6, 4, 8, 0, 7)
    addScaled(0, 8, 4, 8, 0, 7)
    addScaled(-5, 8, -6, 9, 6, 9)
    addScaled(-5, 8, 5, 8, 6, 9)

    addScaled(-1.5, 13, -2, 14, -2, 19)
    addScaled(-1.5, 13, -1, 14, -1, 19)
    addScaled(-1.5, 13, -2, 19, -1, 19)
    addScaled(-2, 19, -2, 18, -6, 18)
    addScaled(-2, 19, -6, 18, -6.5, 19)
    addScaled(1.5, 13, 2, 14, 2, 19)
    addScaled(1.5, 13, 1, 14, 1, 19)
    addScaled(1.5, 13, 2, 19, 1, 19)
    addScaled(2, 19, 2, 18, 6, 18)
    addScaled(2, 19, 6, 18, 6.5, 19)
end

function Lime_Init()
    lime.addEventListener("Lime::OnJoin", onJoin)
    lime.addEventListener("Lime::OnLeave", onLeave)

    Material.addMaterial("stone", 1.0, 1.0, 1.0, 5, 0.7, 0.0)

    createWorld()

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
    --[[
    if pickupCountdown > 0 then
        pickupCountdown = pickupCountdown - timeDelta
        if pickupCountdown < 0 then
            pickupCountdown = pickupCountdownMax
            spawnPickup();
        end
    end
    ]]
end

local timeDelta

function Lime_Update(timeDelta_)
    updateTimers(timeDelta_)
    timeDelta = timeDelta_
end

function Lime_PostUpdate()
    for user, playerID in pairs(connectedUsers) do
        local focusX = lime.getAttribute(playerID, attribLykkeFocusX)
        local focusY = lime.getAttribute(playerID, attribLykkeFocusY)
        Camera.addFocusPoint(focusX, focusY)
    end

    Camera.update(timeDelta)

    local translationX, translationY = Camera.getTranslation()
    local scaleX, scaleY = Camera.getScale()

    for userID in pairs(connectedUsers) do
        lime.setCameraTranslation(userID, translationX, translationY)
        lime.setCameraScale(userID, scaleX, scaleY)
    end
end

function Lime_Clean()

end
