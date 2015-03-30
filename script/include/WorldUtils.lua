
lime.include("Material")

function addTerrain(material, x1, y1, x2, y2, x3, y3)
	lime.startComponent()
	lime.setInitialPosition(0.0, 0.0)
	lime.setInitialAngle(0.0)
	lime.setComponentType("static")
	lime.startShape("triangle-group")
	applyMaterial(material)
	lime.addShapeTriangle(x1, y1, x2, y2, x3, y3)
	lime.endShape()
	local terrainID = lime.endComponent()

	-- NOTE: Terrain is currently never cleaned
	--       It doesn't really need to be cleaned, but it would be nice if it was
end
