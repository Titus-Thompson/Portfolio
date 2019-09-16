import rhinoscriptsyntax as rhino
import math
x = float(0.0)
y = float(1.0)
z = float(0.0)
dt = float(0.0001)
t = float(0.0)
iterations = 1000000
points = []
num_points = 1000
for i in range(iterations):
dx = float(10*(y-x)) * dt
dy = float(28*x - y - x * z) * dt
dz = float(x*y - float(8/3)*z) * dt
# dt = dt * 0.9
if i % (iterations / num_points) == 0:
points.append([x, y, z])
x = x + dx
y = y + dy
z = z + dz
t = t + dt
for p in points:
rhino.AddPoint(p)
rhino.AddInterpCurve(points)