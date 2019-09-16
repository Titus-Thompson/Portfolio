import rhinoscriptsyntax as rhino
import math

obj = rhino.GetObject("Select object to transform", preselect=True)

def rotateXY(theta):
    rotation =  [[math.cos(theta), -math.sin(theta),0,0],
            [math.sin(theta), math.cos(theta),0,0],
            [0,0,1,0],
            [0,0,0,1]]
    return rotation
def rotateXZ(theta):
    rotation =  [[math.cos(theta),0, -math.sin(theta),0],
            [0,1,0,0],
            [math.sin(theta),0, math.cos(theta),0],
            [0,0,0,1]]
    return rotation
def stretch(x, y, z):
    stc = [[x,0,0,0],
            [0,y,0,0],
            [0,0,z,0],
            [0,0,0,1]]
    return stc

phi = 1.61803398875
turn = rotateXY((2*math.pi)/phi)
xyRotation = turn

copies = 45
for i in range(copies):
    t = float(i)/float(copies - 1)
    xzAngle = t*t*(math.pi*9.0/24.0) + math.pi*1.0/24.0
    xzRotation = rotateXZ(xzAngle)
    widen = stretch(1, 1 + t*t*2.5, 1)
    
    tsfm = rhino.XformMultiply(xyRotation, xzRotation)
    tsfm = rhino.XformMultiply(tsfm, widen)
    
    
    new_obj = rhino.TransformObject(obj, tsfm, copy=True)
    xyRotation = rhino.XformMultiply(xyRotation, turn)
