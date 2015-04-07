
local C = lime.module("Deathmatch/C")

local attribEntityPosX = C.attribEntityPosX
local attribEntityPosY = C.attribEntityPosY
local attribEntityAngle = C.attribEntityAngle
local attribEntityParent = C.attribEntityParent
local attribEntityCollector = C.attribEntityCollector
local attribEntityDamageable = C.attribEntityDamageable
local attribEntityHealth = C.attribEntityHealth
local attribEntityOnDamaged = C.attribEntityOnDamaged
local attribLykkeFocusX = C.attribLykkeFocusX
local attribLykkeFocusY = C.attribLykkeFocusY
local attribLykkeAbilityWallSlide = C.attribLykkeAbilityWallSlide
local attribLykkeAbilityWallJump = C.attribLykkeAbilityWallJump
local attribBulletSpeed = C.attribBulletSpeed
local attribBulletTimeout = C.attribBulletTimeout
local attribBulletRadius = C.attribBulletRadius
local attribBulletOnHit = C.attribBulletOnHit

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

-- Movement states
local isMoving = false
local movingDirection = 1
local movingTime = 0
local jumpRise = false
local wallSliding = false
local allowWallJump = false

local resetNextUpdate = false

local entityID
local userOwner

local mainCompo
local mainShape
local cameraFocusCompo
local compos = {}
local joints = {}

local function reset()
    resetNextUpdate = true
end

local function onDamaged()
    local health = lime.getAttribute(entityID, attribEntityHealth)

    if health < 0 then
        reset()
    end
end

local function loadDefaultProperties()
    lime.setAttribute(entityID, attribEntityCollector, true)
    lime.setAttribute(entityID, attribEntityDamageable, true)
    lime.setAttribute(entityID, attribEntityHealth, 100)
    lime.setAttribute(entityID, attribEntityOnDamaged, onDamaged)

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
    lime.setShapeDensity(1.4)
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
    lime.setShapeColor(1.0, 1.0, 1.0, 1.0)
    lime.setShapeAnimation("gamemode/Deathmatch/Player")
    lime.setShapeAnimationSelection("still")
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

-- A function that applies movement forces
-- and updates movement state
local function move(timeDelta)

    lime.selectComponent(mainCompo)
    lime.setInputData(userOwner)

    -- Fetch sensor data

    hasGround = sensorGround > 0
    hasWallLeft = sensorWallLeft > 0
    hasWallRight = sensorWallRight > 0

    -- ----------------
    -- Code for jumping
    -- ----------------

    if lime.getKeyPress(lime.KEY_W) or lime.getKeyPress(lime.KEY_UP) then
        local jumpDir = -2

        -- If we have ground, or walls on both sides, jump straight up
        if (hasGround) or (hasWallLeft and hasWallRight) then jumpDir = 0
        else
            local allowWallJump = lime.getAttribute(entityID, attribLykkeAbilityWallJump) > 0

            -- If we're allowed to jump and have wall left, jump right
            -- Otherwise, if we're allowed to jump and have wall right, jump left
            if allowWallJump and hasWallLeft then jumpDir = 1
            elseif allowWallJump and hasWallRight then jumpDir = -1 end
        end

        if jumpDir ~= -2 then
            local verticalJump = cLykkeJumpImpulseY
            local horizontalJump = 0.0
            if jumpDir ~= 0 then
                -- If we aren't jumping straight up, add X force and dampen Y force
                verticalJump = verticalJump * cLykkeWallJumpImpulseYM
                horizontalJump = cLykkeWallJumpImpulseX * jumpDir
            end

            lime.applyLinearImpulseToCenter(horizontalJump, verticalJump)
        end
    end

    -- ------------------------------------
    -- Code for walking and movement states
    -- ------------------------------------

    lime.selectComponent(mainCompo)

    -- Target velocity is the final velocity we want to reach while moving

    local targetVelocity = 0

    -- First determine if we're moving at all

    isMoving = false
    if lime.getKeyState(lime.KEY_A) or lime.getKeyState(lime.KEY_LEFT) then
        isMoving = true
        movingDirection = -1
    end
    if lime.getKeyState(lime.KEY_D) or lime.getKeyState(lime.KEY_RIGHT) then
        isMoving = true
        movingDirection = 1
    end

    -- If we're moving, change the target velocity and update movingTime
    -- Otherwise, set movingTime to 0 (movingTime is how long we've been moving for)

    if isMoving then
        targetVelocity = cLykkeMaxVelocityX * movingDirection
        movingTime = movingTime + timeDelta
    else
        movingTime = 0
    end

    -- We'll apply force to correct the X velocity
    -- If on ground, multiply it by ground acceleration, otherwise by air acceleration

    local velocityX = lime.getLinearVelocity()
    local inputForce = targetVelocity - velocityX
    if hasGround then
        inputForce = inputForce * cLykkeGroundAcceleration
    else
        inputForce = inputForce * cLykkeAirAcceleration
    end

    -- Apply the inputForce and some additional Y force, because it makes jumping look nicer
    -- NOTE: additional Y force must not be too high, because the player
    --       starts to jitter at that point

    lime.applyForceToCenter(inputForce, -20.0)

    local velocityX, velocityY = lime.getLinearVelocity()
    local newVelocityX = velocityX
    local newVelocityY = velocityY

    -- We are rising if we aren't touching the ground and our Y velocity is greater than 0
    -- The "not hasGround" check is important because Y velocity can be greater that 0
    -- even while standing on flat ground because of JBox2D position corrections.

    jumpRise = not hasGround and velocityY > 0

    -- Check if we are wall sliding
    -- We are wall sliding if we are allowed to wall slide, don't have ground, are near a wall, and aren't rising
    -- If we are wall sliding, dampen the Y velocity a bit

    local canWallSlide = lime.getAttribute(entityID, attribLykkeAbilityWallSlide) > 0
    wallSliding = canWallSlide and not hasGround and (hasWallLeft or hasWallRight) and not jumpRising
    if wallSliding then
        newVelocityY = velocityY - velocityY * timeDelta * cLykkeWallSlideVelYM
    end

    lime.setLinearVelocity(newVelocityX, newVelocityY)

    -- After all that moving, change the movingDirection state to the direction of the velocity
    local velocityX, velocityY = lime.getLinearVelocity()
    if velocityX < -0.75 then
        movingDirection = -1
    elseif velocityX > 0.75 then
        movingDirection = 1
    end

    -- If we fell out of the world or died, reset
    local positionX, positionY = lime.getComponentPosition()
    if positionY < -10 or resetNextUpdate then
        lime.setAttribute(entityID, attribEntityHealth, 100)
        lime.selectComponent(mainCompo)
        -- TODO: set position to real origin, not to a hardcoded position
        lime.setComponentPosition(0, 4 * 1.5)
        lime.setLinearVelocity(0, 0)

        lime.selectComponent(cameraFocusCompo)
        positionX, positionY = lime.getComponentPosition()

        resetNextUpdate = false
    end

    -- Finally, update the focus point
    lime.setAttribute(entityID, attribLykkeFocusX, positionX)
    lime.setAttribute(entityID, attribLykkeFocusY, positionY)
end

local function shoot(timeDelta)
    Gun.update(gun, timeDelta)

    if lime.getMouseState(lime.MOUSE_BUTTON_LEFT) then
        if Gun.shoot(gun) then
            lime.selectComponent(mainCompo)
            local posx, posy = lime.getComponentPosition()
            local mx, my = lime.getMousePosition()

            if mx < posx then
                movingDirection = -1
            else
                movingDirection = 1
            end

            posx = posx - 0.7 * -movingDirection
            posy = posy - 0.125

            local bullet = lime.newEntity()
            lime.setAttribute(bullet, attribEntityParent, entityID)
            lime.setAttribute(bullet, attribEntityPosX, posx)
            lime.setAttribute(bullet, attribEntityPosY, posy)
            lime.setAttribute(bullet, attribEntityAngle, math.atan2(my - posy, mx - posx))
            lime.setAttribute(bullet, attribBulletSpeed, 10)
            lime.setAttribute(bullet, attribBulletTimeout, 2)
            lime.setAttribute(bullet, attribBulletRadius, 0.125)
            lime.setAttribute(bullet, attribBulletOnHit, function(body)
                lime.selectComponent(body)
                local owner = lime.getOwner()
                local health = lime.getAttribute(owner, attribEntityHealth)
                local onDamaged = lime.getAttribute(owner, attribEntityOnDamaged)

                lime.setAttribute(owner, attribEntityHealth, health - 10)
                onDamaged()
            end)
            lime.assignScript(bullet, "Deathmatch/Bullet")
        end
    end
end

local function animate()
    lime.selectComponent(mainCompo)
    lime.selectShape(mainShape)
    lime.setShapeAnimationRoot(0.0, -cLykkeHeight + 0.1)
    lime.setShapeAnimationScale(0.4 * -movingDirection, 0.4)

    local animationSelection = "still"
    if hasGround then
        if not isMoving then
            animationSelection = "still"
        else
            animationSelection = "walking"
        end
    elseif wallSliding then
        animationSelection = "wallSliding"
    else
        if not isMoving then
            if jumpRise then
                local velocityX = lime.getLinearVelocity()
                if velocityX < -0.75 or velocityX > 0.75 then
                    animationSelection = "fallingLeft"
                else
                    animationSelection = "falling"
                end
            else
                animationSelection = "falling"
            end
        else
            animationSelection = "fallingLeft"
        end
    end
    lime.setShapeAnimationSelection(animationSelection)
    
    lime.updateShape()
end

function Lime_Update(timeDelta)
    move(timeDelta)
    shoot(timeDelta)
    animate()
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
