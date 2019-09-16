import rhinoscriptsyntax as rhino

r1 = (float)(3/4)
r2 = (float)(2/3)
iterations = 4

def draw_parametric_surface(param_range_u, param_range_v, num_points_u = 10, num_points_v = 10):
  points = []
  fractal = mountain_fractal
#  print fractal(0,0)
  for i in range(num_points_u):
    u = param_range_u[0] + (param_range_u[1] - param_range_u[0]) * float(i)/float(num_points_u - 1)
    for j in range(num_points_v):
      v = param_range_v[0] + (param_range_v[1] - param_range_v[0]) * float(j)/float(num_points_v - 1)
      #print fractal(u,v)
      points.append( evaluate(fractal,u,v) )
      rhino.AddPoint( evaluate(fractal,u,v))
  rhino.AddSrfControlPtGrid((num_points_u, num_points_v), points)

def evaluate(seeds, u, v):
#  minval = min(functions, u,v)
#  print functions
#  minval = functions(u, v)[0]
  minval = 10000000
  for f in seeds:
    val = mountain(f[0],f[1],f[2], u, v)
    if minval > val:
      minval = val
  return (u, v, minval)

def mountain_fractal(u,v):
  mtns = []
  mtns.append([0,0,0])
  mtns.extend(append_mountains([0, 0, 0], 0, iterations))
  mtns.extend(append_mountains([0, 0, 0], 1, iterations))
  mtns.extend(append_mountains([0, 0, 0], 2, iterations))
  mtns.extend(append_mountains([0, 0, 0], 3, iterations))
  return mtns

def append_mountains(tip, orientation, remaining_iter):
  mtns = []
  n = iterations - remaining_iter
  for newOrientation in range(4):
    xdir = 2*({0: 0, 1: 1, 2: 0, 3: 1}[newOrientation]) - 1
    ydir = 2*({0: 0, 1: 0, 2: 1, 3: 1}[newOrientation]) - 1
    if orientation % 4 == newOrientation:
      newTip = [tip[0] + xdir*(pow(r1,n) + pow(r1,n+1)), tip[1] + ydir*(pow(r1,n) + pow(r1,n+1)), tip[2] + pow(r2,n) + pow(r2,n+1)]
    else:
      newTip = [tip[0] + xdir*pow(r1,n), tip[1] + ydir*pow(r1,n), tip[2] + pow(r2,n)]
    if (orientation - 2) % 2 != newOrientation:
      mtns.append([newTip[0], newTip[1], newTip[2]])
      if remaining_iter > 0:
        mtns.extend(append_mountains(newTip, newOrientation, remaining_iter - 1))
  return mtns

def mountain(xoff, yoff, zoff, u, v):
  return 3*max(abs(u + xoff), abs(v + yoff)) + zoff

draw_parametric_surface((-100,100), (-100,100))