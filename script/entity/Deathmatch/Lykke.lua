
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

local NinjaMoves = lime.module("common/ninja/NinjaMoves")
NinjaMoves.setPhysicsData(cLykkeMaxVelocityX, cLykkeGroundAcceleration, cLykkeAirAcceleration, cLykkeJumpImpulseY,
                          cLykkeWallJumpImpulseX, cLykkeWallJumpImpulseYM, cLykkeWallSlideVelYM)

local NinjaFace = lime.module("common/ninja/NinjaFace")
NinjaFace.setRoot(0.0, -cLykkeHeight + 0.1)
NinjaFace.setScale(0.4, 0.4)
NinjaFace.setSelections("still", "walking", "falling", "fallingLeft", "wallSliding")

local Gun = lime.module("Deathmatch/Gun")
local gun = Gun.create()

-- Sensor data
local sensorGround = 0
local sensorWallLeft = 0
local sensorWallRight = 0

local hasGround
local hasWallLeft
local hasWallRight

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

    NinjaMoves.setUserData(mainCompo, userOwner)
    NinjaFace.setNinja(mainCompo, mainShape, NinjaMoves)

    print("created player [ID=" .. entityID .. "] for user [ID=" .. userOwner .. "]")
end

local function tryReset()
    lime.selectComponent(mainCompo)
    lime.applyForceToCenter(0, -20.0)

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
end

local function shoot(timeDelta)
    local isMoving, isWallSliding, isRising, movementDirection = NinjaMoves.getState()

    Gun.update(gun, timeDelta)

    if lime.getMouseState(lime.MOUSE_BUTTON_LEFT) then
        if Gun.shoot(gun) then
            lime.selectComponent(mainCompo)
            local posx, posy = lime.getComponentPosition()
            local mx, my = lime.getMousePosition()

            if mx < posx then
                --movingDirection = -1
            else
                --movingDirection = 1
            end

            posx = posx - 0.7 * -movementDirection
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

function Lime_Update(timeDelta)
    local allowWallJump = lime.getAttribute(entityID, attribLykkeAbilityWallJump) > 0
    local allowWallSlide = lime.getAttribute(entityID, attribLykkeAbilityWallSlide) > 0
    NinjaMoves.setAbilities(true, true, allowWallJump, allowWallSlide)

    hasGround = sensorGround > 0
    hasWallLeft = sensorWallLeft > 0
    hasWallRight = sensorWallRight > 0
    NinjaMoves.setSensorData(hasGround, hasWallLeft, hasWallRight)

    NinjaMoves.move(timeDelta)

    tryReset()
    shoot(timeDelta)

    NinjaFace.animate()
end

function Lime_PostUpdate()
    lime.selectComponent(mainCompo)
    local positionX, positionY = lime.getComponentPosition()
    lime.setAttribute(entityID, attribLykkeFocusX, positionX)
    lime.setAttribute(entityID, attribLykkeFocusY, positionY)
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
