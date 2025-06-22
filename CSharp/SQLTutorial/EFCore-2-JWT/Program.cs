using System.Diagnostics;
using System.Reflection;
using EFCore_2_JWT.Context;
using EFCore_2_JWT.Service;
using EFCore_2_JWT.Service.IService;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policyBuilder =>
    {
        policyBuilder.AllowAnyHeader().AllowAnyMethod().AllowAnyOrigin();
    });
});

// 注册DbContext 服务
builder.Services.AddDbContext<EfCoreContext>(options =>
{
    var connectionString = builder.Configuration.GetConnectionString("BunnyTest");
    options.UseMySQL(connectionString);
});

// Add services to the container.
builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "My API", Version = "v1" });
    // ⭐ 添加 XML 注释支持
    var xmlFile = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
    var xmlPath = Path.Combine(AppContext.BaseDirectory, xmlFile);
    c.IncludeXmlComments(xmlPath);
});

// 依赖注入
builder.Services.AddTransient<IUserService, UserService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.MapSwagger();
    app.UseSwaggerUI();
}

// 创建数据库
using (var serviceScope = app.Services.CreateScope())
{
    var efCoreContext = serviceScope.ServiceProvider.GetRequiredService<EfCoreContext>();
    var dbCreated = efCoreContext.Database.EnsureCreatedAsync();
    Debug.WriteLine(dbCreated.Result ? "Database created successfully!" : "Database already exists.");
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();