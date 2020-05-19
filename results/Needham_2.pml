active proctype ModelS() {
   atomic { x1_1 := 2; y1_1 := 3; }
   atomic { x2_1 := 4; y2_1 := 5; }
   atomic { x_2 := pubKey(y1_1); y_2 := y1_1; }
   atomic { x_4 := pubKey(y2_1); y_4 := y2_1; }
   atomic { x_3 := I2; y_3 := 2; }
   atomic { x_5 := I4; y_5 := 4; }
   atomic { v1_1 := 3; w1_1 := y_3; }
   atomic { v2_1 := 5; w2_1 := y_5; }
   atomic { v_3 := pubKey(w1_1); w_3 := y1_1; }
   atomic { v_5 := pubKey(w2_1); w_5 := y2_1; }
   atomic { v_2 := x_3; w_2 := I3; }
   atomic { v_4 := x_5; w_4 := I5; }
   atomic { z_3 := w_2; }
   atomic { z_5 := w_4; }
   goto LEnd;

LEnd:
}