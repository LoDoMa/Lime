
-- Constants
local radius = 0.5
local sensorGroundName = "GroundSensor"
local sensorWallLeftName = "WallSensorLeft"
local sensorWallRightName = "WallSensorRight"

-- Property names
local property_canCollectPickups = "canCollectPickups"
local property_maxVelocityX = "maxVelocityX"
local property_groundAcceleration = "groundAcceleration"
local property_airAcceleration = "airAcceleration"
local property_wallClimbing = "wallClimbing"
local property_horizontalJumpMultiplier = "horizontalJumpMultiplier"
local property_verticalJumpMultiplier = "verticalJumpMultiplier"
local property_wallJumpVerticalMultiplier = "wallJumpVerticalMultiplier"
local property_groundVelocityMultiplier = "groundVelocityMultiplier"
local property_wallSlidingVelocityMultiplier = "wallSlidingVelocityMultiplier"

-- Sensor and jump data
local sensorGround = 0
local sensorWallLeft = 0
local sensorWallRight = 0
local hasGround = false
local hasWallLeft = false
local hasWallRight = false
local wallSliding = false
local allowWallJump = false

local entityID
local userOwner

local mainCompo
local cameraFocusCompo
local compos = {}
local joints = {}

local function loadDefaultProperties()
    lime.setAttribute(entityID, property_canCollectPickups, true)
    lime.setAttribute(entityID, property_maxVelocityX, 30)
    lime.setAttribute(entityID, property_groundAcceleration, 5)
    lime.setAttribute(entityID, property_airAcceleration, 0.5)
    lime.setAttribute(entityID, property_wallClimbing, 0)
    lime.setAttribute(entityID, property_horizontalJumpMultiplier, 8)
    lime.setAttribute(entityID, property_verticalJumpMultiplier, 17)
    lime.setAttribute(entityID, property_wallJumpVerticalMultiplier, 0.7)
    lime.setAttribute(entityID, property_groundVelocityMultiplier, 10)
    lime.setAttribute(entityID, property_wallSlidingVelocityMultiplier, 17.7)
end

local function registerCompo(compoID) compos[compoID] = true end
local function registerJoint(jointID) joints[jointID] = true end

local function sensorBegin(contact)
    if contact.fixtureA == sensorGroundName then
        sensorGround = sensorGround + 1
    elseif contact.fixtureA == sensorWallLeftName then
        sensorWallLeft = sensorWallLeft + 1
    elseif contact.fixtureA == sensorWallRightName then
        sensorWallRight = sensorWallRight + 1
    end
end

local function sensorEnd(contact)
    if contact.fixtureA == sensorGroundName then
        sensorGround = sensorGround - 1
    elseif contact.fixtureA == sensorWallLeftName then
        sensorWallLeft = sensorWallLeft - 1
    elseif contact.fixtureA == sensorWallRightName then
        sensorWallRight = sensorWallRight - 1
    end
end

local function createSensor(name, offx, offy)
    lime.startShape("circle")
    lime.setShapeName(name)
    lime.setShapeOffset(offx * radius, offy * radius)
    lime.setShapeSolid(false)
    lime.setShapeDensity(0.0)
    lime.setShapeFriction(0.0)
    lime.setShapeRestitution(0.0)
    lime.setShapeRadius(0.05)
    lime.endShape()
end

local function createBody()
    lime.startComponent()
    lime.setInitialPosition(0.0, 0.0)
    lime.setInitialAngle(0.0)
    lime.setComponentType("dynamic")
    
    -- body
    lime.startShape("triangle-group")
    lime.setShapeDensity(0.9)
    lime.setShapeFriction(0.0)
    lime.setShapeRestitution(0.0)
    lime.addShapeTriangle(-radius, -radius, -radius, radius, radius, -radius)
    lime.addShapeTriangle(radius, radius, -radius, radius, radius, -radius)
    lime.setShapeColor(1.0, 1.0, 1.0, 1.0)
    lime.setShapeTexture("gamemode/Deathmatch/Player")
    lime.endShape()
    
    -- sensors
    createSensor(sensorGroundName, 0, -1);
    createSensor(sensorWallLeftName, -1, 0);
    createSensor(sensorWallRightName, 1, 0);

    mainCompo = lime.endComponent()

    lime.selectComponent(mainCompo)
    lime.setLinearDamping(0.6)
    lime.setAngularDamping(0.1)
    lime.setAngleLocked(true)
    lime.setUsingCCD(false)
    lime.setOwner(entityID)

    lime.addContactListener(nil, nil, sensorBegin, sensorEnd, mainCompo, nil)

    registerCompo(mainCompo)
    cameraFocusCompo = mainCompo
end

function Lime_Init(entityID_)
    entityID = entityID_
    userOwner = lime.getAndClearAttribute(entityID, "owner")

    loadDefaultProperties()
    createBody()

    print("created player [ID=" .. entityID .. "] for user [ID=" .. userOwner .. "]")
end

-- Return values:
--    -2 = can't jump
--    -1 = can jump left
--     0 = can jump up
--     1 = can jump right
local function jumpDirection()
    if hasGround then return 0 end
    if hasWallLeft and hasWallRight then return 0 end
    if hasWallLeft or hasWallRight then allowWallJump = false end
    if hasWallLeft then return 1 end
    if hasWallRight then return -1 end
    return -2
end

local function updateJumpData()
    hasGround = sensorGround > 0
    if sensorGround > 0 then
        allowWallJump = true
    end

    local wallClimber = lime.getAttribute(entityID, property_wallClimbing) > 0
    hasWallLeft = (allowWallJump or wallClimber) and sensorWallLeft > 0
    hasWallRight = (allowWallJump or wallClimber) and sensorWallRight > 0
end

local function tryJump()
    updateJumpData()

    lime.setInputData(userOwner)
    if lime.getKeyPress(lime.KEY_W) then
        local jumpDir = jumpDirection()
        if jumpDir ~= -2 then
            local verticalJumpMultiplier = lime.getAttribute(entityID, property_verticalJumpMultiplier)
            local horizontalJumpMultiplier = lime.getAttribute(entityID, property_horizontalJumpMultiplier)
            if jumpDir ~= 0 then
                local wallJumpMultiplier = lime.getAttribute(entityID, property_wallJumpVerticalMultiplier)
                verticalJumpMultiplier = verticalJumpMultiplier * wallJumpMultiplier
            end

            lime.selectComponent(mainCompo)
            lime.applyLinearImpulseToCenter(jumpDir * horizontalJumpMultiplier, verticalJumpMultiplier)
        end
    end
end

local function move(timeDelta)
    local maxVelocityX = lime.getAttribute(entityID, property_maxVelocityX)
    local groundAcceleration = lime.getAttribute(entityID, property_groundAcceleration)
    local airAcceleration = lime.getAttribute(entityID, property_airAcceleration)

    lime.selectComponent(mainCompo)
    local velocityX = lime.getLinearVelocity()

    local inputForce = 0
    if lime.getKeyState(lime.KEY_A) then inputForce = -maxVelocityX - velocityX end
    if lime.getKeyState(lime.KEY_D) then inputForce = maxVelocityX - velocityX end

    if hasGround then
        inputForce = inputForce * groundAcceleration
    else
        inputForce = inputForce * airAcceleration
    end

    lime.applyForceToCenter(inputForce, -20.0)

    local velocityX, velocityY = lime.getLinearVelocity()
    local newVelocityX = velocityX
    local newVelocityY = velocityY

    if hasGround then
        local groundVelocityMultiplier = lime.getAttribute(entityID, property_groundVelocityMultiplier)
        newVelocityX = velocityX - velocityX * timeDelta * groundVelocityMultiplier
    end

    if not hasGround and (hasWallLeft or hasWallRight) and velocityY < 0 then
        local wallSlidingVelocityMultiplier = lime.getAttribute(entityID, property_wallSlidingVelocityMultiplier)
        newVelocityY = velocityY - velocityY * timeDelta * wallSlidingVelocityMultiplier

        wallSliding = true
    end

    lime.setLinearVelocity(newVelocityX, newVelocityY)
end

function Lime_Update(timeDelta)
    tryJump()
    move(timeDelta)

    lime.selectComponent(cameraFocusCompo)
    local cfx, cfy = lime.getComponentPosition()

    lime.setAttribute(entityID, "focusX", cfx)
    lime.setAttribute(entityID, "focusY", cfy)
end

function Lime_PostUpdate()

end

function Lime_Clean()
    for compoID in pairs(compos) do
        lime.removeComponent(compoID)
        compos[compoID] = nil
    end

    for jointID in pairs(joints) do
        lime.removeJoint(jointID)
        compos[jointID] = nil
    end

    print("removed player [ID=" .. entityID .. "] for user [ID=" .. userOwner .. "]")
end
