<template>
  <nav><RouterLink to="/products">商品</RouterLink><RouterLink to="/orders">订单</RouterLink></nav>
  <RouterView />
</template>

<style>
body { margin: 0; background: #f7f9fc; font-family: system-ui, 'Microsoft YaHei', sans-serif; color: #172033 }
nav { display: flex; gap: 1.5rem; padding: 1rem max(1.5rem, calc((100vw - 1132px) / 2)); background: #172033 }
nav a { color: #cbd5e1; text-decoration: none }
nav a.router-link-active { color: white; font-weight: 700 }
</style>
