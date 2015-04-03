
local Gun = lime.module("Gun")

local gun = Gun.create()

-- Constants
local radiusw = 0.25
local radiush = 0.5

-- Property names
local property_canGetShot = "canGetShot"
local property_canCollectPickups = "canCollectPickups"
local property_maxVelocityX = "maxVelocityX"
local property_groundAcceleration = "groundAcceleration"
local property_airAcceleration = "airAcceleration"
local property_wallClimbing = "wallClimbing"
local property_horizontalJumpMultiplier = "horizontalJumpMultiplier"
local property_verticalJumpMultiplier = "verticalJumpMultiplier"
local property_wallJumpVerticalMultiplier = "wallJumpVerticalMultiplier"
local property_wallSlidingVelocityMultiplier = "wallSlidingVelocityMultiplier"

-- Sensor and jump data
local sensorGround = 0
local sensorWallLeft = 0
local sensorWallRight = 0
local hasGround = false
local hasWallLeft = false
local hasWallRight = false
local wallSliding = false
local isMoving = false
local movingDirection = 1
local movingTime = 0
local jumpRise = false
local allowWallJump = false

local entityID
local userOwner

local mainCompo
local mainShape
local cameraFocusCompo
local compos = {}
local joints = {}

local function loadDefaultProperties()
    lime.setAttribute(entityID, property_canGetShot, true)
    lime.setAttribute(entityID, property_canCollectPickups, true)
    lime.setAttribute(entityID, property_maxVelocityX, 12)
    lime.setAttribute(entityID, property_groundAcceleration, 10)
    lime.setAttribute(entityID, property_airAcceleration, 1.5)
    lime.setAttribute(entityID, property_wallClimbing, 1)
    lime.setAttribute(entityID, property_horizontalJumpMultiplier, 8)
    lime.setAttribute(entityID, property_verticalJumpMultiplier, 17)
    lime.setAttribute(entityID, property_wallJumpVerticalMultiplier, 0.7)
    lime.setAttribute(entityID, property_wallSlidingVelocityMultiplier, 8.7)
end

local function registerCompo(compoID) compos[compoID] = true end
local function registerJoint(jointID) joints[jointID] = true end

local function sensorBegin(contact)
    if contact.shapeA == sensorGroundID then
        sensorGround = sensorGround + 1
    elseif contact.shapeA == sensorWallLeftID then
        sensorWallLeft = sensorWallLeft + 1
    elseif contact.shapeA == sensorWallRightID then
        sensorWallRight = sensorWallRight + 1
    end
end

local function sensorEnd(contact)
    if contact.shapeA == sensorGroundID then
        sensorGround = sensorGround - 1
    elseif contact.shapeA == sensorWallLeftID then
        sensorWallLeft = sensorWallLeft - 1
    elseif contact.shapeA == sensorWallRightID then
        sensorWallRight = sensorWallRight - 1
    end
end

local function createSensor(offx, offy)
    local sensorID = lime.newShape("circle")
    lime.selectShape(sensorID)
    lime.setShapeOffset(offx * radiusw, offy * radiush)
    lime.setShapeSolid(false)
    lime.setShapeDensity(0.0)
    lime.setShapeFriction(0.0)
    lime.setShapeRestitution(0.0)
    lime.setShapeRadius(0.05)
    lime.updateShape()
    return sensorID;
end

local function createBody()
    mainCompo = lime.newComponent("dynamic", 1.5, 1.5, 0.0)
    lime.selectComponent(mainCompo)
    lime.setLinearDamping(0.6)
    lime.setAngularDamping(0.1)
    lime.setAngleLocked(true)
    lime.setUsingCCD(false)
    lime.setOwner(entityID)

    -- body
    mainShape = lime.newShape("triangle-group")
    lime.selectShape(mainShape)
    lime.setShapeDensity(2.2)
    lime.setShapeFriction(0.0)
    lime.setShapeRestitution(0.0)

    local lssw = radiusw * 0.3
    local lssh = radiush * 0.3
    lime.addShapeTriangle(-radiusw + lssw, -radiush, -radiusw + lssw, radiush, radiusw - lssw, -radiush)
    lime.addShapeTriangle(radiusw - lssw, radiush, -radiusw + lssw, radiush, radiusw - lssw, -radiush)
    lime.addShapeTriangle(-radiusw, -radiush + lssh, -radiusw, radiush - lssh, radiusw, -radiush + lssh)
    lime.addShapeTriangle(radiusw, radiush - lssh, -radiusw, radiush - lssh, radiusw, -radiush + lssh)
    lime.addShapeTriangle(-radiusw, -radiush + lssh, -radiusw + lssw, -radiush, -radiusw + lssw, -radiush + lssh)
    lime.addShapeTriangle(radiusw, -radiush + lssh, radiusw - lssw, -radiush, radiusw - lssw, -radiush + lssh)
    lime.addShapeTriangle(-radiusw, radiush - lssh, -radiusw + lssw, radiush, -radiusw + lssw, radiush - lssh)
    lime.addShapeTriangle(radiusw, radiush - lssh, radiusw - lssw, radiush, radiusw - lssw, radiush - lssh)
    lime.setShapeColor(0.5, 0.5, 0.5, 1.0)
    lime.updateShape()
    
    -- sensors
    sensorGroundID = createSensor(0, -1);
    sensorWallLeftID = createSensor(-1, 0);
    sensorWallRightID = createSensor(1, 0);

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
    if lime.getKeyPress(lime.KEY_W) or lime.getKeyPress(lime.KEY_UP) then
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

    local targetVelocity = 0
    isMoving = false
    if lime.getKeyState(lime.KEY_A) or lime.getKeyState(lime.KEY_LEFT) then
        isMoving = true
        movingDirection = -1
        targetVelocity = -maxVelocityX
    end
    if lime.getKeyState(lime.KEY_D) or lime.getKeyState(lime.KEY_RIGHT) then
        isMoving = true
        movingDirection = 1
        targetVelocity = maxVelocityX
    end

    if not isMoving then
        movingTime = 0
    else
        movingTime = movingTime + timeDelta
    end

    local inputForce = targetVelocity - velocityX

    if hasGround then
        inputForce = inputForce * groundAcceleration
    else
        inputForce = inputForce * airAcceleration
    end

    lime.applyForceToCenter(inputForce, -20.0)

    local velocityX, velocityY = lime.getLinearVelocity()
    local newVelocityX = velocityX
    local newVelocityY = velocityY

    wallSliding = false
    jumpRise = velocityY > 0
    if not hasGround and (hasWallLeft or hasWallRight) and velocityY < 0 then
        local wallSlidingVelocityMultiplier = lime.getAttribute(entityID, property_wallSlidingVelocityMultiplier)
        newVelocityY = velocityY - velocityY * timeDelta * wallSlidingVelocityMultiplier

        wallSliding = true
    end

    lime.setLinearVelocity(newVelocityX, newVelocityY)
end

local function shoot(timeDelta)
    Gun.update(gun, timeDelta)

    if lime.getMouseState(lime.MOUSE_BUTTON_LEFT) then
        if Gun.shoot(gun) then
            lime.selectComponent(mainCompo)
            local posx, posy = lime.getComponentPosition()
            local mx, my = lime.getMousePosition()

            local bullet = lime.newEntity()
            lime.setAttribute(bullet, "parent", entityID)
            lime.setAttribute(bullet, "timeout", 2)
            lime.setAttribute(bullet, "angle", math.atan2(my - posy, mx - posx))
            lime.setAttribute(bullet, "posx", posx)
            lime.setAttribute(bullet, "posy", posy)
            lime.assignScript(bullet, "Bullet")
        end
    end
end

function Lime_Update(timeDelta)
    tryJump()
    move(timeDelta)
    shoot(timeDelta)

    lime.selectComponent(cameraFocusCompo)
    local cfx, cfy = lime.getComponentPosition()

    if cfy < -10 then
        lime.selectComponent(mainCompo)
        lime.setComponentPosition(1.5, 1.5)

        lime.selectComponent(cameraFocusCompo)
        cfx, cfy = lime.getComponentPosition()
    end

    lime.setAttribute(entityID, "focusX", cfx)
    lime.setAttribute(entityID, "focusY", cfy)

    lime.selectShape(mainShape)

    if hasGround then
        if not isMoving then
            lime.setShapeAnimation("gamemode/Deathmatch/PlayerStill.san")
            lime.setShapeAnimationRoot(0.0, -radiush + 0.15)
            lime.setShapeAnimationScale(0.4 * -movingDirection, 0.4)
        else
            lime.setShapeAnimation("gamemode/Deathmatch/PlayerWalking.san")
            lime.setShapeAnimationRoot(0.0, -radiush + 0.15)
            lime.setShapeAnimationScale(0.4 * -movingDirection, 0.4)
        end
        lime.updateShape()
    elseif wallSliding then
        lime.setShapeAnimation("gamemode/Deathmatch/PlayerWallSliding.san")
        if hasWallLeft then
            lime.setShapeAnimationRoot(-0.1, -radiush + 0.15)
            lime.setShapeAnimationScale(0.4, 0.4)
        else
            lime.setShapeAnimationRoot(0.1, -radiush + 0.15)
            lime.setShapeAnimationScale(-0.4, 0.4)
        end
        lime.updateShape()
    else
        if not isMoving then
            if jumpRise then
                lime.selectComponent(mainCompo)
                local velocityX = lime.getLinearVelocity()
                if velocityX < -0.75 or velocityX > 0.75 then
                    lime.setShapeAnimation("gamemode/Deathmatch/PlayerFallingLeft.san")
                    lime.setShapeAnimationRoot(0.0, -radiush + 0.15)
                    if velocityX < -0.75 then
                        lime.setShapeAnimationScale(0.4, 0.4)
                    else
                        lime.setShapeAnimationScale(-0.4, 0.4)
                    end
                else
                    lime.setShapeAnimation("gamemode/Deathmatch/PlayerJumpRising.san")
                    lime.setShapeAnimationRoot(0.0, -radiush + 0.15)
                    lime.setShapeAnimationScale(0.4, 0.4)
                end
            else
                lime.setShapeAnimation("gamemode/Deathmatch/PlayerFalling.san")
                lime.setShapeAnimationRoot(0.0, -radiush + 0.15)
                lime.setShapeAnimationScale(0.4, 0.4)
            end
        else
            lime.setShapeAnimation("gamemode/Deathmatch/PlayerFallingLeft.san")
            lime.setShapeAnimationRoot(0.0, -radiush + 0.15)
            lime.setShapeAnimationScale(0.4 * -movingDirection, 0.4)
        end
        lime.updateShape()
    end
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
