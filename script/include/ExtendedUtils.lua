
function string.startsWith(str, start)
   return string.sub(str, 1, string.len(start)) == start
end

function string.endsWith(str, ending)
   return ending == '' or string.sub(str, -string.len(ending)) == ending
end

__LIME_IncludeTable__ = {}
