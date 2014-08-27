
local strict = getStrict()
strictRequireJava("net.lodoma.lime.util.Vector2")

local function buildVector(x, y)
	strict.typecheck.lua(x, "number", 1, "lime.util.vector.new")
	strict.typecheck.lua(y, "number", 2, "lime.util.vector.new")
	return {x = x, y = y}
end

local function isVector(vecTable)
	if(type(vecTable) ~= "table") then return false end
	if(type(vecTable.x) ~= "number") then return false end
	if(type(vecTable.y) ~= "number") then return false end
	return true;
end

local function checkVectorType(value, argument, name)
	local argument_type = type(argument)
	local name_type = type(name)
	assert(argument_type == "number", "invalid argument #2 to \"strict utility checkVectorType\", expected number, got " .. argument_type)
	assert(name_type == "string", "invalid argument #3 to \"strict utility checkVectorType\", expected string, got " .. name_type)
	assert(isVector(value), "invalid argument #" .. argument .. " to \"" .. name .. "\", expected vector2")
end

local function toJava(luaVector)
	checkVectorType(luaVector, 1, "strict utility toJava")
	return strict.java["net.lodoma.lime.util.Vector2"]:new(luaVector.x, luaVector.y)
end

local function toLua(javaVector)
	strict.typecheck.lua(javaVector, "userdata", 1, "strict utility toLua")
	return buildVector(javaVector:getX(), javaVector:getY())
end

addToStrict({
	typecheck = {
		vector = checkVectorType,
	},
	vector = {
		toJava = toJava,
		toLua = toLua,
	},
})

addToLime({
	util = {
		vector = {
			new = buildVector,
			check = isVector,
		},
	},
})