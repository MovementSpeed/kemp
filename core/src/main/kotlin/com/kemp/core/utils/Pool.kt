package com.kemp.core.utils

object Pool {
    private val float3Pool = mutableListOf<Float3>()
    private val float4Pool = mutableListOf<Float4>()

    fun useFloat3(amount: Int, a: (List<Float3>) -> Unit) {
        if (float3Pool.size < amount) repeat(amount - float3Pool.size) {
            float3Pool.add(Float3())
        }

        a(float3Pool)
    }

    fun useFloat3(): Float3 {
        if (float3Pool.size < 1) float3Pool.add(Float3())
        return float3Pool[0]
    }

    fun useFloat4(amount: Int, a: (List<Float4>) -> Unit) {
        if (float4Pool.size < amount) repeat(amount - float4Pool.size) {
            float4Pool.add(Float4())
        }

        a(float4Pool)
    }

    fun useFloat4(): Float4 {
        if (float4Pool.size < 1) float4Pool.add(Float4())
        return float4Pool[0]
    }
}