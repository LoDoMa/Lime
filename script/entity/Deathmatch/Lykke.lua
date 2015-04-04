
local C = lime.module("Deathmatch/C")

local attribEntityPosX = C.attribEntityPosX
local attribEntityPosY = C.attribEntityPosY
local attribEntityAngle = C.attribEntityAngle
local attribEntityParent = C.attribEntityParent
local attribEntityCollector = C.attribEntityCollector
local attribEntityDamageable = C.attribEntityDamageable
local attribEntityHealth = C.attribEntityHealth
local attribLykkeFocusX = C.attribLykkeFocusX
local attribLykkeFocusY = C.attribLykkeFocusY
local attribLykkeAbilityWallSlide = C.attribLykkeAbilityWallSlide
local attribLykkeAbilityWallJump = C.attribLykkeAbilityWallJump
local attribBulletSpeed = C.attribBulletSpeed
local attribBulletTimeout = C.attribBulletTimeout
local attribBulletRadius = C.attribBulletRadius

local cLykkeWidth = C.cLykkeWidth
local cLykkeHeight = C.cLykkeHeight
local cLykkeMaxVelocityX = C.cLykkeMaxVelocityX
local cLykkeGroundAcceleration = C.cLykkeGroundAcceleration
local cLykkeAirAcceleration = C.cLykkeAirAcceleration
local cLykkeJumpImpulseY = C.cLykkeJumpImpulseY
local cLykkeWallJumpImpulseX = C.cLykkeWallJumpImpulseX
local cLykkeWallJumpImpulseYM = C.cLykkeWallJumpImpulseYM
local cLykkeWallSlideVelYM = C.cLykkeWallSlideVelYM

local Gun = lime.module("Deathmatch/Gun")
local gun = Gun.create()

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
    lime.setAttribute(entityID, attribEntityCollector, true)
    lime.setAttribute(entityID, attribEntityDamageable, true)
    lime.setAttribute(entityID, attribEntityHealth, 100)

    lime.setAttribute(entityID, attribLykkeAbilityWallJump, 1)
    lime.setAttribute(entityID, attribLykkeAbilityWallSlide, 1)
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
    lime.setShapeOffset(offx * cLykkeWidth, offy * cLykkeHeight)
    lime.setShapeSolid(false)
    lime.setShapeDensity(0.0)
    lime.setShapeFriction(0.0)
    lime.setShapeRestitution(0.0)
    lime.setShapeRadius(0.05)
    lime.updateShape()
    return sensorID;
end

local function createBody()
    local posx = lime.getAndClearAttribute(entityID, attribEntityPosX)
    local posy = lime.getAndClearAttribute(entityID, attribEntityPosY)
    mainCompo = lime.newComponent("dynamic", posx, posy, 0.0)
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

    local lssw = cLykkeWidth * 0.3
    local lssh = cLykkeHeight * 0.3
    lime.addShapeTriangle(-cLykkeWidth + lssw, -cLykkeHeight, -cLykkeWidth + lssw, cLykkeHeight, cLykkeWidth - lssw, -cLykkeHeight)
    lime.addShapeTriangle(cLykkeWidth - lssw, cLykkeHeight, -cLykkeWidth + lssw, cLykkeHeight, cLykkeWidth - lssw, -cLykkeHeight)
    lime.addShapeTriangle(-cLykkeWidth, -cLykkeHeight + lssh, -cLykkeWidth, cLykkeHeight - lssh, cLykkeWidth, -cLykkeHeight + lssh)
    lime.addShapeTriangle(cLykkeWidth, cLykkeHeight - lssh, -cLykkeWidth, cLykkeHeight - lssh, cLykkeWidth, -cLykkeHeight + lssh)
    lime.addShapeTriangle(-cLykkeWidth, -cLykkeHeight + lssh, -cLykkeWidth + lssw, -cLykkeHeight, -cLykkeWidth + lssw, -cLykkeHeight + lssh)
    lime.addShapeTriangle(cLykkeWidth, -cLykkeHeight + lssh, cLykkeWidth - lssw, -cLykkeHeight, cLykkeWidth - lssw, -cLykkeHeight + lssh)
    lime.addShapeTriangle(-cLykkeWidth, cLykkeHeight - lssh, -cLykkeWidth + lssw, cLykkeHeight, -cLykkeWidth + lssw, cLykkeHeight - lssh)
    lime.addShapeTriangle(cLykkeWidth, cLykkeHeight - lssh, cLykkeWidth - lssw, cLykkeHeight, cLykkeWidth - lssw, cLykkeHeight - lssh)
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
    userOwner = lime.getAndClearAttribute(entityID, attribEntityParent)

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

    local wallJump = lime.getAttribute(entityID, attribLykkeAbilityWallJump) > 0
    hasWallLeft = (allowWallJump or wallJump) and sensorWallLeft > 0
    hasWallRight = (allowWallJump or wallJump) and sensorWallRight > 0
end

local function tryJump()
    updateJumpData()

    lime.setInputData(userOwner)
    if lime.getKeyPress(lime.KEY_W) or lime.getKeyPress(lime.KEY_UP) then
        local jumpDir = jumpDirection()
        if jumpDir ~= -2 then
            local verticalJumpMultiplier = cLykkeJumpImpulseY
            local horizontalJumpMultiplier = 0.0
            if jumpDir ~= 0 then
                verticalJumpMultiplier = verticalJumpMultiplier * cLykkeWallJumpImpulseYM
                horizontalJumpMultiplier = cLykkeWallJumpImpulseX * jumpDir
            end

            lime.selectComponent(mainCompo)
            lime.applyLinearImpulseToCenter(horizontalJumpMultiplier, verticalJumpMultiplier)
        end
    end
end

local function move(timeDelta)
    lime.selectComponent(mainCompo)
    local velocityX = lime.getLinearVelocity()

    local targetVelocity = 0
    isMoving = false
    if lime.getKeyState(lime.KEY_A) or lime.getKeyState(lime.KEY_LEFT) then
        isMoving = true
        movingDirection = -1
        targetVelocity = -cLykkeMaxVelocityX
    end
    if lime.getKeyState(lime.KEY_D) or lime.getKeyState(lime.KEY_RIGHT) then
        isMoving = true
        movingDirection = 1
        targetVelocity = cLykkeMaxVelocityX
    end

    if not isMoving then
        movingTime = 0
    else
        movingTime = movingTime + timeDelta
    end

    local inputForce = targetVelocity - velocityX

    if hasGround then
        inputForce = inputForce * cLykkeGroundAcceleration
    else
        inputForce = inputForce * cLykkeAirAcceleration
    end

    lime.applyForceToCenter(inputForce, -20.0)

    local velocityX, velocityY = lime.getLinearVelocity()
    local newVelocityX = velocityX
    local newVelocityY = velocityY

    wallSliding = false
    jumpRise = velocityY > 0

    local wallSlide = lime.getAttribute(entityID, attribLykkeAbilityWallSlide) > 0
    if wallSlide and not hasGround and (hasWallLeft or hasWallRight) and velocityY < 0 then
        newVelocityY = velocityY - velocityY * timeDelta * cLykkeWallSlideVelYM

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
            lime.setAttribute(bullet, attribEntityParent, entityID)
            lime.setAttribute(bullet, attribEntityPosX, posx)
            lime.setAttribute(bullet, attribEntityPosY, posy)
            lime.setAttribute(bullet, attribEntityAngle, math.atan2(my - posy, mx - posx))
            lime.setAttribute(bullet, attribBulletSpeed, 10)
            lime.setAttribute(bullet, attribBulletTimeout, 2)
            lime.setAttribute(bullet, attribBulletRadius, 0.05)
            lime.assignScript(bullet, "Deathmatch/Bullet")
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

    lime.setAttribute(entityID, attribLykkeFocusX, cfx)
    lime.setAttribute(entityID, attribLykkeFocusY, cfy)

    lime.selectShape(mainShape)

    if hasGround then
        if not isMoving then
            lime.setShapeAnimation("gamemode/Deathmatch/PlayerStill.lua")
            lime.setShapeAnimationRoot(0.0, -cLykkeHeight + 0.1)
            lime.setShapeAnimationScale(0.4 * -movingDirection, 0.4)
        else
            lime.setShapeAnimation("gamemode/Deathmatch/PlayerWalking.lua")
            lime.setShapeAnimationRoot(0.0, -cLykkeHeight + 0.1)
            lime.setShapeAnimationScale(0.4 * -movingDirection, 0.4)
        end
        lime.updateShape()
    elseif wallSliding then
        lime.setShapeAnimation("gamemode/Deathmatch/PlayerWallSliding.lua")
        if hasWallLeft then
            lime.setShapeAnimationRoot(-0.1, -cLykkeHeight + 0.1)
            lime.setShapeAnimationScale(0.4, 0.4)
        else
            lime.setShapeAnimationRoot(0.1, -cLykkeHeight + 0.1)
            lime.setShapeAnimationScale(-0.4, 0.4)
        end
        lime.updateShape()
    else
        if not isMoving then
            if jumpRise then
                lime.selectComponent(mainCompo)
                local velocityX = lime.getLinearVelocity()
                if velocityX < -0.75 or velocityX > 0.75 then
                    lime.setShapeAnimation("gamemode/Deathmatch/PlayerFallingLeft.lua")
                    lime.setShapeAnimationRoot(0.0, -cLykkeHeight + 0.1)
                    if velocityX < -0.75 then
                        lime.setShapeAnimationScale(0.4, 0.4)
                    else
                        lime.setShapeAnimationScale(-0.4, 0.4)
                    end
                else
                    lime.setShapeAnimation("gamemode/Deathmatch/PlayerFalling.lua")
                    lime.setShapeAnimationRoot(0.0, -cLykkeHeight + 0.1)
                    lime.setShapeAnimationScale(0.4, 0.4)
                end
            else
                lime.setShapeAnimation("gamemode/Deathmatch/PlayerFalling.lua")
                lime.setShapeAnimationRoot(0.0, -cLykkeHeight + 0.1)
                lime.setShapeAnimationScale(0.4, 0.4)
            end
        else
            lime.setShapeAnimation("gamemode/Deathmatch/PlayerFallingLeft.lua")
            lime.setShapeAnimationRoot(0.0, -cLykkeHeight + 0.1)
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
