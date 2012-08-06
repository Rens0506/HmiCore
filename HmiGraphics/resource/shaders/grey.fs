// specular.fs
//
// per-pixel specular lighting

varying vec3 N, L;

void main(void)
{
    const float specularExp = 128.0;
    vec3 NN = normalize(N);
    vec3 NL = normalize(L);
    vec3 NH = normalize(NL + vec3(0.0, 0.0, 1.0));

    // calculate diffuse lighting
    float intensity = max(0.0, dot(NN, NL));
    vec3 diffuse = gl_Color.rgb * intensity;

    // calculate specular lighting
    vec3 specular = vec3(0.0);
    if (intensity > 0.0)
    {
        intensity = max(0.0, dot(NN, NH));
        specular = vec3(pow(intensity, specularExp));
    }
    vec3 col = diffuse + specular;
    float grey = dot(col, vec3(0.299, 0.587, 0.114));
    gl_FragColor = vec4(grey, grey, grey, 1.0);
    
}
