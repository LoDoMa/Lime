
-- Constants
local radius = 0.35

-- Property names
local property_canCollectPickups = "canCollectPickups"

local entityID
local compoID
local applyEffects

local removeNextUpdate = false

local function onContact(contact)
    lime.selectComponent(contact.bodyB)
    local owner = lime.getOwner()
    if owner then
        if lime.getAttribute(owner, property_canCollectPickups) == true then
            if applyEffects then
                applyEffects(owner)
            end
            contact.setEnabled(false)
            removeNextUpdate = true
        end
    end
end

local function createBody()
    local posx = lime.getAndClearAttribute(entityID, "posx")
    local posy = lime.getAndClearAttribute(entityID, "posy")

    lime.startComponent()
    lime.setInitialPosition(posx, posy)
    lime.setInitialAngle(0.0)
    lime.setComponentType("dynamic")
    
    -- body
    lime.startShape("triangle-group")
    lime.setShapeDensity(0.9)
    lime.setShapeFriction(0.2)
    lime.setShapeRestitution(0.05)
    lime.addShapeTriangle(-radius, -radius, -radius, radius, radius, -radius)
    lime.addShapeTriangle(radius, radius, -radius, radius, radius, -radius)
    lime.endShape()

    compoID = lime.endComponent()

    lime.selectComponent(compoID)
    lime.setLinearDamping(0.6)
    lime.setAngularDamping(0.1)

    lime.addContactListener(onContact, nil, nil, nil, compoID, nil)
end

function Lime_Init(entityID_)
    entityID = entityID_
    applyEffects = lime.getAndClearAttribute(entityID, "applyEffects")

    createBody()
end

function Lime_Update(timeDelta)
    if removeNextUpdate then
        lime.removeEntity(entityID)
    end
end

function Lime_PostUpdate()
    
end

function Lime_Clean()
    lime.removeComponent(compoID)
end
