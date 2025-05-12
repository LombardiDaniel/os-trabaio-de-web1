package main

import (
	"fmt"
	"math/rand/v2"
	"time"
)

type Carga struct {
	Q  float32
	Ts time.Time
}

func main() {
	var entries []Carga
	for i := range 4 {
		entries = append(entries, Carga{
			Q:  rand.Float32() * 100,
			Ts: time.Now().AddDate(0, 0, i),
		})
	}
	fmt.Println("Cargas:")
	for _, v := range entries {
		fmt.Printf("%+v\n", v)
	}

	fmt.Println("###")

	var restos []Carga
	for i, v := range entries {
		disponivel := v.Q + agruparRestos(restos, v.Ts)
		envios := int(disponivel / 22)
		r := v.Q - float32(envios*22)
		fmt.Printf("[%d]::Eviado: %d; sobrou %.2f\n", i, envios, r)
		restos = append(restos, Carga{
			Q:  r,
			Ts: v.Ts,
		})
	}
}

func agruparRestos(r []Carga, currDate time.Time) float32 {
	valids := []Carga{}
	for _, v := range r {
		if currDate.Sub(v.Ts).Hours() > 5*24 {
			valids = append(valids, v)
		}
	}

	avail := float32(0.0)
	for _, v := range valids {
		avail += v.Q
	}

	return avail
}

func ABC() {
	a := MyStruct{}
	for _, item := range a {
		item.DoIteration()
	}
}
