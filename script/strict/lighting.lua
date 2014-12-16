
local strict = getStrict()

-- lighting is currently server-only
if not strict.network.side.server then
	return
end

strictRequireJava("net.lodoma.lime.shader.light.BasicLight")

local workingLight = nil

-- local utilities

local function checkWorkingElementSet(element, name, call)
	local name_type = type(name)
	local call_type = type(call)
	assert(name_type == "string", "invalid argument #2 to \"local utility checkWorkingElementSet\", expected string, got " .. name_type)
	assert(call_type == "string", "invalid argument #3 to \"local utility checkWorkingElementSet\", expected string, got " .. call_type)
	assert(element ~= nil, "\"" .. name .. "\" not set before calling \"" .. call .. "\"")
end

-- creation

local function createBasicLight(position, radius, color, angleFrom, angleTo)
	strict.typecheck.vector(position, 2, "lime.light.createBasic")
	strict.typecheck.lua(radius, "number", 3, "lime.light.createBasic")
	strict.typecheck.color(color, 4, "lime.light.createBasic")
	strict.typecheck.lua(angleFrom, "number", 5, "lime.light.createBasic")
	strict.typecheck.lua(angleTo, "number", 6, "lime.light.createBasic")

	local javaPosition = strict.vector.toJava(position)
	local javaColor = strict.color.toJava(color)

	workingLight = strict.java["net.lodoma.lime.shader.light.BasicLight"]:new(javaPosition, radius, javaColor, angleFrom, angleTo)
end

-- adding

local function addLight()
	checkWorkingElementSet(workingLight, "light", "lime.light.add")

	--[[ we're using newLight here instead of adding directly to the pool
	     because newLight also sends packages ]]
	return strict.world:newLight(workingLight)
end

local function getLight(identifier)
	checkWorkingElementSet(workingLight, "light", "lime.light.get")

	strict.world.lightPool:get(identifier)
end

local function removeLight()
	checkWorkingElementSet(workingLight, "light", "lime.light.remove")

	strict.world.lightPool:remove(workingLight)
end

addToLime({
	light = {
		add = addLight,
		get = getLight,
		remove = removeLight,
		createBasic = createBasicLight,
	},
})