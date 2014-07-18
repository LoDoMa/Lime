-- initialization

local entity = LIME_INIT
local HashHelper = java.require("net.lodoma.lime.util.HashHelper")

-- entity interaction

-- entity world interaction

local function getEntityByID(id)
	entityWorld = entity:getWorld()
	return entityWorld:getEntity(id)
end

-- body interaction

-- joint interaction

-- mask interaction

-- property interaction

local function setProperty(propertyName)
	print(propertyName)
end

-- utilities

local function round(num, idp)
  local mult = 10^(idp or 0)
  return math.floor(num * mult + 0.5) / mult
end

-- global tables

Lime = {
	this = entity,
	-- entity world interaction
	getEntityByID = getEntityByID,
	-- body interaction
	-- joint interaction
	-- mask interaction
	-- property interaction
	setProperty = setProperty,				-- NOTE: used by Lime, should not be called
	-- utilities
	round = round;
}