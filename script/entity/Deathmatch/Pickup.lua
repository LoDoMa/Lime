
local C = lime.module("Deathmatch/C")

local attribEntityCollector = C.attribEntityCollector

local cPickupWidth = C.cPickupWidth

local entityID
local compoID
local applyEffects

local applied = false
local removeNextUpdate = false

local function onContact(contact)
    lime.selectComponent(contact.bodyB)
    local owner = lime.getOwner()
    if owner then
        if lime.getAttribute(owner, attribEntityCollector) == true then
            if not applied and applyEffects then
                applyEffects(owner)
                applied = true
            end
            contact.setEnabled(false)
            removeNextUpdate = true
        end
    end
end

local function createBody()
    local posx = lime.getAndClearAttribute(entityID, "posx")
    local posy = lime.getAndClearAttribute(entityID, "posy")

    compoID = lime.newComponent("dynamic", posx, posy, 0.0f)
    lime.selectComponent(compoID)
    lime.setLinearDamping(0.6)
    lime.setAngularDamping(0.1)
    
    local radius = cPickupWidth / 2.0
    
    -- body
    local shapeID = lime.newShape("triangle-group")
    lime.selectShape(shapeID)
    lime.setShapeDensity(0.9)
    lime.setShapeFriction(0.2)
    lime.setShapeRestitution(0.05)
    lime.addShapeTriangle(-radius, -radius, -radius, radius, radius, -radius)
    lime.addShapeTriangle(radius, radius, -radius, radius, radius, -radius)
    lime.setShapeColor(1.0, 0.2, 0.3, 1.0)
    lime.updateShape()

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
