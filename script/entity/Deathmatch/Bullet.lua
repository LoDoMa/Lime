
local entityID
local compoID

local parentID
local velocityX
local velocityY
local timeout

local removeNextUpdate = false

local function contactListener(contact)
    contact.setEnabled(false)

    lime.selectComponent(contact.bodyB)
    local owner = lime.getOwner()
    if owner then
        if owner == parentID then return end
        removeNextUpdate = true

        if lime.getAttribute(owner, C.attribEntityDamageable) == true then
            print("POOF")
        end
    else
        removeNextUpdate = true
    end
end

local function createBody()
    local posx = lime.getAndClearAttribute(entityID, "posx")
    local posy = lime.getAndClearAttribute(entityID, "posy")

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
    lime.setShapeRadius(0.05)
    lime.setShapeColor(0.5, 0.5, 0.5, 1.0)
    lime.updateShape()

    lime.addContactListener(contactListener, nil, nil, nil, compoID, nil)
end

function Lime_Init(entityID_)
    entityID = entityID_
    createBody()

    local angle = lime.getAndClearAttribute(entityID, "angle")
    timeout = lime.getAndClearAttribute(entityID, "timeout")
    parentID = lime.getAndClearAttribute(entityID, "parent")
    velocityX = math.cos(angle) * 10
    velocityY = math.sin(angle) * 10
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
