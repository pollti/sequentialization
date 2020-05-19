import numpy as np
import matplotlib.pyplot as plt
from scipy import optimize as opt
#import math

data = np.transpose(np.loadtxt("timess.tpt"))
x = data[0]
y = data[1] #data[1,::2]
print(type(x))

log_y = np.log(y)

# Exponential fitting
e_fit = np.polyfit(x, log_y, 1)

print("Exponential approximation:")
print(e_fit)

e_fct = np.exp(e_fit[1]) * np.exp(e_fit[0]*x)

# Polynomial fitting
def f1(x, b, a):
    return abs(a) + abs(b)*x
def f2(x, c, b, a):
    return abs(a) + abs(b)*x + abs(c)*x*np.log(x)
def f3(x, d, c, b, a):
    return abs(a) + abs(b)*x + abs(c)*x*np.log(x) + abs(d)*x**2
def f4(x, c, b, a):
    return abs(a) + abs(b)*x + abs(c)*x**2
def f5(x, a):
    return a*x**2

# Positive coefficients
coefficients1 = np.abs(np.append(opt.curve_fit(f1, x[386:766], y[386:766],maxfev=1000000)[0], []))
coefficients2 = np.abs(np.append(opt.curve_fit(f2, x[386:766], y[386:766],maxfev=1000000)[0], []))
coefficients3 = np.abs(np.append(opt.curve_fit(f3, x[386:766], y[386:766],maxfev=1000000)[0], []))
coefficients4 = np.abs(np.append(opt.curve_fit(f4, x[386:766], y[386:766],maxfev=1000000)[0], []))
# Postivie coefficients, based on 1-200
factor = y[300:]/(x[300:]**2)
a = max(factor)
b = min(factor)
coefficients32 = [a, 0, 0]
coefficients42 = [b, 0, 0]

#coefficients32 = np.abs(np.append(opt.curve_fit(f5, [x[3],x[6],x[12],x[24],x[48],x[96],x[192],x[384],x[789]], [y[3],y[6],y[12],y[24],y[48],y[96],y[192],y[384],y[789]],maxfev=1000000)[0], []))
coefficients32 = np.abs(np.append(opt.curve_fit(f5, [x[47],x[95],x[191],x[383],x[768]], [y[47],y[95],y[191],y[383],y[768]],maxfev=1000000)[0], []))
a = coefficients32[0]
#coefficients42 = np.abs(np.append(opt.curve_fit(f5, [x[2],x[5],x[11],x[23],x[47],x[95],x[191],x[383],x[787]], [y[2],y[5],y[11],y[23],y[47],y[95],y[191],y[383],y[787]],maxfev=1000000)[0], []))
coefficients42 = np.abs(np.append(opt.curve_fit(f5, [x[46],x[94],x[190],x[382],x[766]], [y[46],y[94],y[190],y[382],y[766]],maxfev=1000000)[0], []))
b = coefficients42[0]
print(a,b)
# All coefficients, based on 1-200
#coefficients43 = np.polyfit(x[:200], y[:200], 4)

new_yapprox = np.array([b,4*b])
i = 3
while i <= 768:
    coef = [1/3*(4*b-a),0,(4/3*a-4/3*b)*i**2]
    apf = np.poly1d(coef)
    new_yapprox = [*new_yapprox, *apf(np.arange(i,2*i,1).tolist())]
    i *= 2

new_yapprox = np.array(new_yapprox[:1000])

print("Polynomial approximations:")
print(*coefficients1)
print(*coefficients2)
print(*coefficients3)
print(*coefficients4)
print("200 first value based:")
print(*coefficients32)
print(*coefficients42)
#print("200 first value based, non-non-negative:")
#print(*coefficients43)

poly1 = np.poly1d(coefficients1)
poly2 = np.poly1d(coefficients2)
poly3 = np.poly1d(coefficients3)
poly4 = np.poly1d(coefficients4)
poly32 = np.poly1d(coefficients32)
poly42 = np.poly1d(coefficients42)
#poly43 = np.poly1d(coefficients42)

new_x = x #np.linspace(x[0], x[-1])
new_y1 = f1(new_x,*coefficients1)
new_y2 = f2(new_x,*coefficients2)
new_y3 = f3(new_x,*coefficients3)
new_y4 = poly4(new_x)
#new_y32 = poly32(new_x)
#new_y42 = poly42(new_x)
new_y32 = f5(new_x,*coefficients32)
new_y42 = f5(new_x,*coefficients42)
#new_y43 = poly43(new_x)

# Least squares
print("Least squares:")
print(np.sum((y-e_fct)**2))
print(np.sum((y[386:766]-new_y1[386:766])**2))
print(np.sum((y[386:766]-new_y2[386:766])**2))
print(np.sum((y[386:766]-new_y3[386:766])**2))
print(np.sum((y[386:766]-new_y4[386:766])**2))
print(np.sum((np.array([y[2],y[5],y[11],y[23],y[47],y[95],y[191],y[383],y[768]])-np.array([new_y32[2],new_y32[5],new_y32[11],new_y32[23],new_y32[47],new_y32[95],new_y32[191],new_y32[383],new_y32[768]]))**2))
print(np.sum((np.array([y[1],y[4],y[10],y[22],y[46],y[94],y[190],y[382],y[766]])-np.array([new_y42[1],new_y42[4],new_y42[10],new_y42[22],new_y42[46],new_y42[94],new_y42[190],new_y42[382],new_y42[766]]))**2))
print(np.sum((y-new_yapprox)**2))

#print(np.sum((y-new_y43)**2))

plt.plot(x, y, "o")
#plt.plot(new_x, new_y1, new_x, new_y2, new_x, new_y3, new_x, new_y4)
plt.plot(new_x, new_y32, new_x, new_y42, new_x, new_yapprox)
#plt.plot(new_x, new_y43)
#plt.plot(x, e_fct)

# Write function values in file
out = np.transpose([x,y/1000,e_fct/1000,new_y1/1000,new_y2/1000,new_y3/1000,new_y4/1000,new_y32/1000,new_y42/1000,new_yapprox/1000])
f = open("functionvaluess2.csv", "w")
f.write("n t exp p1 p2 p3 p4 p32 p42 ap \n")
for x in out:
    for y in x:
        f.write(str(y))
        f.write(" ")
    f.write("\n")
f.close()

plt.savefig("graphss2.jpg")
