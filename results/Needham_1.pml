active proctype ModelS() {
   atomic { x1_1 := 2; y1_1 := 3; }
   atomic { x_2 := pubKey(y1_1); y_2 := y1_1; }
   atomic { x_3 := I2; y_3 := 2; }
   atomic { v1_1 := 3; w1_1 := y_3; }
   atomic { v_3 := pubKey(w1_1); w_3 := y1_1; }
   atomic { v_2 := x_3; w_2 := I3; }
   atomic { z_3 := w_2; }
   goto LEnd;

LEnd:
}