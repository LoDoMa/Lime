
local strict = getStrict()
strictRequireJava("net.lodoma.lime.gui.Color")

local function buildColor(r, g, b, a)
	strict.typecheck.lua(r, "number", 1, "lime.util.color.new")
	strict.typecheck.lua(g, "number", 2, "lime.util.color.new")
	strict.typecheck.lua(b, "number", 3, "lime.util.color.new")
	strict.typecheck.lua(a, "number", 4, "lime.util.color.new")
	return {r = r, g = g, b = b, a = a}
end

local function isColor(colTable)
	if(type(colTable) ~= "table") then return false end
	if(type(colTable.r) ~= "number") then return false end
	if(type(colTable.g) ~= "number") then return false end
	if(type(colTable.b) ~= "number") then return false end
	if(type(colTable.a) ~= "number") then return false end
	return true;
end

local function checkColorType(value, argument, name)
	local argument_type = type(argument)
	local name_type = type(name)
	assert(argument_type == "number", "invalid argument #2 to \"local utility checkColorType\", expected number, got " .. argument_type)
	assert(name_type == "string", "invalid argument #3 to \"local utility checkColorType\", expected string, got " .. name_type)
	assert(isColor(value), "invalid argument #" .. argument .. " to \"" .. name .. "\", expected color")
end

local function toJava(luaColor)
	checkColorType(luaColor, 1, "strict utility toJava")
	return strict.java["net.lodoma.lime.gui.Color"]:new(luaColor.r, luaColor.g, luaColor.b, luaColor.a)
end

local function toLua(javaColor)
	strict.typecheck.lua(javaColor, "userdata", 1, "strict utility toLua")
	return buildColor(javaColor:getR(), javaColor:getG(), javaColor:getB(), javaColor:getA())
end

addToStrict({
	typecheck = {
		color = checkColorType,
	},
	color = {
		toJava = toJava,
		toLua = toLua,
	},
})

addToLime({
	util = {
		color = {
			new = buildColor,
			check = isColor,
		},
	},
})