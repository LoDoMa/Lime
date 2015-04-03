
local C = lime.module("Deathmatch/C")

local attribEntityPosX = C.attribEntityPosX
local attribEntityPosY = C.attribEntityPosY
local attribEntityAngle = C.attribEntityAngle
local attribEntityParent = C.attribEntityParent
local attribEntityDamageable = C.attribEntityDamageable
local attribBulletSpeed = C.attribBulletSpeed
local attribBulletTimeout = C.attribBulletTimeout
local attribBulletRadius = C.attribBulletRadius
local attribBulletOnHit = C.attribBulletOnHit
local attribBulletOnWaste = C.attribBulletOnWaste

local entityID
local compoID

local parentID
local velocityX
local velocityY

local timeout

local listenerOnHit
local listenerOnWaste

local removeNextUpdate = false

local function contactListener(contact)
    contact.setEnabled(false)

    lime.selectComponent(contact.bodyB)
    local owner = lime.getOwner()
    if owner then
        if owner == parentID then return end
        removeNextUpdate = true

        if lime.getAttribute(owner, C.attribEntityDamageable) == true then
            if listenerOnHit then
                listenerOnHit(contact.bodyB)
            end
        end
    else
        removeNextUpdate = true
        if listenerOnWaste then
            listenerOnWaste(contact.bodyB)
        end
    end
end

local function createBody()
    local posx = lime.getAndClearAttribute(entityID, attribEntityPosX)
    local posy = lime.getAndClearAttribute(entityID, attribEntityPosY)
    local radius = lime.getAndClearAttribute(entityID, attribBulletRadius)

    compoID = lime.newComponent("dynamic", posx, posy, 0.0)
    lime.selectComponent(compoID)

    lime.setAngleLocked(true)
    lime.setUsingCCD(true)
    lime.setGravityScale(0.0)
    lime.setOwner(entityID)

    local mainShape = lime.newShape("circle")
    lime.selectShape(mainShape)
    lime.setShapeDensity(1.0)
    lime.setShapeFriction(0.0)
    lime.setShapeRestitution(0.0)
    lime.setShapeRadius(radius)
    lime.setShapeColor(0.5, 0.5, 0.5, 1.0)
    lime.updateShape()

    lime.addContactListener(contactListener, nil, nil, nil, compoID, nil)
end

function Lime_Init(entityID_)
    entityID = entityID_
    parentID = lime.getAndClearAttribute(entityID, attribEntityParent)
    timeout = lime.getAndClearAttribute(entityID, attribBulletTimeout)

    listenerOnHit = lime.getAndClearAttribute(entityID, attribBulletOnHit)
    listenerOnWaste = lime.getAndClearAttribute(entityID, attribBulletOnWaste)

    createBody()

    local angle = lime.getAndClearAttribute(entityID, attribEntityAngle)
    local speed = lime.getAndClearAttribute(entityID, attribBulletSpeed)
    velocityX = math.cos(angle) * speed
    velocityY = math.sin(angle) * speed
end

function Lime_Update(timeDelta)
    lime.selectComponent(compoID)
    lime.setLinearVelocity(velocityX, velocityY)

    timeout = timeout - timeDelta
    if (timeout < 0) or removeNextUpdate then
        lime.removeEntity(entityID)
    end
end

function Lime_PostUpdate()

end

function Lime_Clean()
    lime.removeComponent(compoID)
end
