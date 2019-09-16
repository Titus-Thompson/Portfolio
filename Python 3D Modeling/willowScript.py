import rhinoscriptsyntax as RhinoScript
import Rhino
import math
phi = (1.0 + 5.0**0.5)/2.0


start = "N"
iterations = 7
angle = math.pi/12.0
phiangle = 2*math.pi*phi
scaleValue = 1.0/phi
upVctr = Rhino.Geometry.Vector3d(0.0, 0.0, 1.0)

def iterate(string):
    cmds = ""
    
    for c in string:
        if c == 'N':
            cmds = cmds + "[FnR[FvN]R[FvN]R[FvN]R[FvN]R[FvN]R[FvN]]"
        else:
            cmds = cmds + c
    
    return cmds

ID = [[1,0,0,0],
      [0,1,0,0],
      [0,0,1,0],
      [0,0,0,1]]

def translate(x1,x2,x3):
    translation = [[1,0,0,x1],
                   [0,1,0,x2],
                   [0,0,1,x3],
                   [0,0,0,1]]
    return translation
def scale(l):
    translation = [[l,0,0,0],
                   [0,l,0,0],
                   [0,0,l,0],
                   [0,0,0,1]]
    return translation
def rotateXY(sintheta, costheta):
    rotation =  [[costheta, -sintheta,0,0],
                 [sintheta, costheta,0,0],
                 [0,0,1,0],
                 [0,0,0,1]]
    return rotation
def rotateXZ(sintheta,costheta):
    rotation =  [[costheta,0, -sintheta,0],
                 [0,1,0,0],
                 [sintheta,0, costheta,0],
                 [0,0,0,1]]
    return rotation
def rotateYZ(sintheta,costheta):
    rotation =  [[1,0,0,0],
                 [0,costheta, -sintheta,0],
                 [0,sintheta, costheta,0],
                 [0,0,0,1]]
    return rotation
def rotateAroundLine(p1,p2,p3,q1,q2,q3,theta):
    #The procedure in this method is outlined by the textbook
    sinThX = q2/math.sqrt(q2**2 + q3**2)
    cosThX = q3/math.sqrt(q2**2 + q3**2)
    sinThY = q1
    cosThY = math.sqrt(q2**2 + q3**2)
    rotation = RhinoScript.XformMultiply(translate(-p1,-p2,-p3), rotateYZ(sinThX,cosThX))
    rotation = RhinoScript.XformMultiply(rotation, rotateXZ(-sinThY,cosThY))
    rotation = RhinoScript.XformMultiply(rotation, rotateXY(math.sin(theta),math.cos(theta)))
    rotation = RhinoScript.XformMultiply(rotation, rotateXZ(sinThY,cosThY))
    rotation = RhinoScript.XformMultiply(rotation, rotateYZ(-sinThX,cosThX))
    rotation = RhinoScript.XformMultiply(rotation, translate(p1,p2,p3))
    return rotation
def rotateAroundVector(v,theta):
    sinThX = v.Y/math.sqrt(v.Y**2 + v.Z**2)
    cosThX = v.Z/math.sqrt(v.Y**2 + v.Z**2)
    sinThY = v.X
    cosThY = math.sqrt(v.Y**2 + v.Z**2)
    rotation = RhinoScript.XformMultiply(rotateYZ(sinThX,cosThX), rotateXZ(-sinThY,cosThY))
    rotation = RhinoScript.XformMultiply(rotation, rotateXY(math.sin(theta),math.cos(theta)))
    rotation = RhinoScript.XformMultiply(rotation, rotateXZ(sinThY,cosThY))
    rotation = RhinoScript.XformMultiply(rotation, rotateYZ(-sinThX,cosThX))
    return rotation
def orthVector(vect):
    if vect.X == 0 and vect.Y == 0:
        if vect.Z == 0:
            raise ValueError('zero vector')
        return Rhino.Geometry.Vector3d(0.0,1.0,0.0)
    nrmlz = math.sqrt(vect.X**2 + vect.Y**2)
    return Rhino.Geometry.Vector3d(-vect.Y/nrmlz,vect.X/nrmlz,0.0)
def verticalRotate(matrix, vity):
    #Find angle of matrix' verticality
    v = RhinoScript.VectorTransform(upVctr, matrix)
    v = RhinoScript.VectorUnitize(v)
    angV = math.acos(math.sqrt(v.X^2 + v.Y^2))
    #Add preset angle 'vity' times until completely vertical
    angNew = ang + vity*angle
    if ang > math.pi/2.0:
        ang = math.pi/2.0
    elif ang < -math.pi/2.0:
        ang = -math.pi/2.0
    #Compute the new "concrete" rotation
    angRot = angNew - angV
    orth = Rhino.Geometry.Vector3d.CrossProduct(v, upVctr)
    rotation = rotateAroundVector(orth,angRot)
    return rotation


commands = start
for i in range(iterations):
    commands = iterate(commands)


obj = RhinoScript.GetObject("Select object to transform", preselect=True)

bb = RhinoScript.BoundingBox(obj)
length = bb[5].Z - bb[1].Z

turtlePos = [0,0,0]
turtleRelOr = [0,0]      #(xyPlane radians, zAxis radians [from 0 to pi] )
rotationMatrix = ID
fwdVctr = upVctr
nodeNormal = fwdVctr
nodeOrthogonal
spokeCount = 0
verticality = 0
posStack = []
rotationStack = []
relOrientStack = []
absOrientStack = []
fwdStack = []
nodeNormStack = []
nodeOrthStack = []
spokeStack = []
vertStack = []
depth = 1

tsfmStack = []

for c in commands:
    if c == '^':
        verticality = verticality + 1
    elif c == 'v':
        verticality = verticality - 1
    elif c == '>':
        turtleRelOr[0] = turtleRelOr[0] + phiangle
    elif c == '<':
        turtleRelOr[0] = turtleRelOr[0] - phiangle
    elif c == '[':
        posStack.append(turtlePos)
        rotationStack.append(rotationMatrix)
        relOrientStack.append(turtleRelOr)
        fwdStack.append(fwdVctr)
        nodeNormStack.append(nodeNormal)
        nodeOrthStack.append(nodeOrthogonal)
        spokeStack.append(spokeCount)
        vertStack.append(verticality)
        depth = depth + 1
    elif c == ']':
        turtlePos = posStack.pop()
        rotationMatrix = rotationStack.pop()
        turtleRelOr = relOrientStack.pop()
        fwdVctr = fwdStack.pop()
        nodeNormal = nodeNormStack.pop()
        nodeOrthogonal = nodeOrthStack.pop()
        spokeCount = spokeStack.pop()
        verticality = vertStack.pop()
        depth = depth - 1
    elif c == 'n':      #flag for beginning a node
        spokeCount = 0
        nodeNormal = fwdVctr
        nodeOrthogonal = orthVctr(nodeNormal)
        randomRotate = random()*2.0*math.pi
        nodeOrthogonal = RhinoScript.VectorTransform(nodeOrthogonal, rotateAroundVector(nodeNormal,randomRotate))
    elif c == 'R':      #node spoke rotate
        spokeCount = spokeCount + 1
        for i in range(spokeCount):
            fwdVctr = RhinoScript.VectorTransform(nodeOrthogonal, rotateAroundVector(nodeNormal,phiangle))
        randomRotate = math.acos(random()*math.pi)/2.0
        fwdVctr = RhinoScript.VectorTransform(fwdVctr, rotateAroundVector(Rhino.Geometry.Vector3d.CrossProduct(fwdVctr,nodeNormal),randomRotate))
    elif c == 'F':
        #find rotation matrix from old forward vector
        orthVctr = orthVector(fwdVctr)
        relativeRotation = RhinoScript.XformMultiply(rotateAroundVector(fwdVctr,turtleRelOr[0]), rotateAroundVector(orthVctr,turtleRelOr[1]))
        rotationMatrix = RhinoScript.XformMultiply(relativeRotation, rotationMatrix)
        rotationMatrix = verticalRotate(rotationMatrix, verticality)
        
        #find new forward vector from rotation matrix and scale
        fwdVctr = RhinoScript.VectorTransform(upVctr, rotationMatrix)
        fwdVctr = RhinoScript.VectorUnitize(fwdVctr)
        scaleMatrix = ID
        for i in range(depth):
            fwdVctr = RhinoScript.VectorScale(fwdVctr, scaleValue)
            scaleMatrix = RhinoScript.XformMultiply(scaleMatrix, scale(scaleValue))
        
        #add data to transformation stack
        amalgam = RhinoScript.XformMultiply(translate(turtlePos[0],turtlePos[1],turtlePos[2]), rotationMatrix)
        amalgam = RhinoScript.XformMultiply(amalgam, scaleMatrix)
        tsfmStack.append(amalgam)
        
        #move turtle forward
        turtlePos = [turtlePos[0] + fwdVctr.X*length, turtlePos[1] + fwdVctr.Y*length, turtlePos[2] + fwdVctr.Z*length]
        turtleRelOr = [0,0]

#Copy branches into rhino
for matrix in tsfmStack:
    RhinoScript.TransformObject(obj, matrix, True)

