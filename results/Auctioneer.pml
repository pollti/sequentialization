active proctype ModelS() {
   atomic { b_1 := incB1 + 0; }
   atomic { b_3 := b_1; }
   
Lt:
   if
   :: incB2 + b_3 <= maxB2 -> atomic { b_1 := incB2 + b_3; }
                              atomic { b_2 := b_1; }
                              if
                              :: incB1 + b_2 <= maxB1 -> atomic { b_1 := incB1 + b_2; }
                                                         atomic { b_3 := b_1; }
                                                         goto Lt;
                              :: else                 -> atomic { sold_1 := 2; }
                                                         atomic { b_3 := b_1; }
                                                         goto LEnd;
                              fi
   :: else                 -> atomic { sold_1 := 1; }
                              atomic { b_2 := b_1; }
                              goto LEnd;
   fi

LEnd:
}