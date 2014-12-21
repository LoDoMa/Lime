
local strict = getStrict()
strictRequireJava("net.lodoma.lime.shader.light.BasicLight")

local entity = LIME_ENTITY
local world = entity.world

addToStrict({
	entity = entity,
	world = world,
})

-- require "/script/strict/network"
-- require "/script/strict/listener"
-- require "/script/strict/input"

--[[
require "/script/strict/vector"
require "/script/strict/color"
require "/script/strict/lighting"
]]

local workingEntity = nil

-- local utilities

local function checkWorkingElementSet(element, name, call)
	local name_type = type(name)
	local call_type = type(call)
	assert(name_type == "string", "invalid argument #2 to \"local utility checkWorkingElementSet\", expected string, got " .. name_type)
	assert(call_type == "string", "invalid argument #3 to \"local utility checkWorkingElementSet\", expected string, got " .. call_type)
	assert(element ~= nil, "\"" .. name .. "\" not set before calling \"" .. call .. "\"")
end

-- entity

local function setWorkingEntity(entityID)
	strict.typecheck.lua(entityID, "number", 1, "lime.entity.set")

	workingEntity = world.entityPool:get(entityID)
end

-- lime table

addToLime({
	entity = {
		set = setWorkingEntity,
	},
})

