
local strict = getStrict()

local Entity = strictRequireJava("net.lodoma.lime.world.entity.Entity")

local world = LIME_WORLD

addToStrict({
	world = world,
})

-- require "/script/strict/listener"

--[[
require "/script/strict/vector"
require "/script/strict/color"
require "/script/strict/lighting"
]]

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

	return world.entityPool:add(Entity:new(strict.world, hash))
end

-- lime table

addToLime({
	entity = {
		create = addEntityToWorld,
	},
})