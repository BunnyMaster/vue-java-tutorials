using System.Diagnostics;
using System.Reflection;
using EFCore_1_Start.Context;
using EFCore_1_Start.Repositories;
using EFCore_1_Start.Repositories.IRepository;
using EFCore_1_Start.Service;
using EFCore_1_Start.Service.IServices;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policyBuilder =>
    {
        policyBuilder.AllowAnyHeader()
            .AllowAnyMethod()
            .AllowAnyOrigin();
    });
});

// 注册 DbContext 服务
builder.Services.AddDbContext<EfCoreContext>(options =>
{
    var connectionString = builder.Configuration.GetConnectionString("BunnyTest") ?? string.Empty;
    options.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString));
});

// 添加控制器
builder.Services.AddControllers();

// Add services to the container.
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

// 注入依赖
builder.Services.AddTransient<IProductService, ProductService>();
builder.Services.AddScoped<IProductRepository, ProductRepository>();
builder.Services.AddTransient<IPCategoryService, PCategoryService>();
builder.Services.AddScoped<IPCategoryRepository, PCategoryRepository>();

var app = builder.Build();

// 初始化数据库（Code First）
using (var scope = app.Services.CreateScope())
{
    var dbContext = scope.ServiceProvider.GetRequiredService<EfCoreContext>();

    // 1. ⚠️ 注意：EnsureDeleted() 会 永久删除整个数据库，仅适用于开发和测试环境！
    // var ensureDeleted = dbContext.Database.EnsureDeleted();
    // Console.WriteLine(ensureDeleted ? "Database created successfully!" : "Database already exists.");

    // 2. 如果数据库不存在，则创建（仅用于开发环境）
    var dbCreated = dbContext.Database.EnsureCreatedAsync();
    Debug.WriteLine(await dbCreated ? "Database created successfully!" : "Database already exists.");
}

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.MapSwagger();
    app.UseSwaggerUI();
}

// app.UseHttpsRedirection();
app.UseCors();
app.UseAuthorization();
app.MapControllers();
app.Run();