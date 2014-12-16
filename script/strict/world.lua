
local strict = getStrict()

local world = LIME_WORLD
local propertyPool = world:getPropertyPool()

addToStrict({
	world = world,
	propertyPool = propertyPool,
	emanPool = emanPool,
})

require "/script/strict/vector"
require "/script/strict/color"
require "/script/strict/network"
require "/script/strict/listener"
require "/script/strict/lighting"

if lime.network.side.server then
	addToStrict({userManager = strict.propertyPool:getProperty("userManager")})
end

-- local utilities

local function checkWorkingElementSet(element, name, call)
	local name_type = type(name)
	local call_type = type(call)
	assert(name_type == "string", "invalid argument #2 to \"local utility checkWorkingElementSet\", expected string, got " .. name_type)
	assert(call_type == "string", "invalid argument #3 to \"local utility checkWorkingElementSet\", expected string, got " .. call_type)
	assert(element ~= nil, "\"" .. name .. "\" not set before calling \"" .. call .. "\"")
end

-- entity

local function addEntityToWorld(hash)
	strict.typecheck.lua(hash, "number", 1, "lime.entity.create")

	if lime.network.side.server then
		return world:newEntity(hash)
	elseif lime.network.side.client then

	else
		assert(false, "internal problem: unknown network side")
	end
end

-- actor

local function setActorForUser(entityID, userID)
	world:setActor(entityID, userID)
end

-- lime table

addToLime({
	entity = {
		create = addEntityToWorld,
	},
	actor = {
		set = setActorForUser,
	},
})